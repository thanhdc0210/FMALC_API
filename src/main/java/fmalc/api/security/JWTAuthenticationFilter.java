package fmalc.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fmalc.api.dto.LoginResponseDTO;
import fmalc.api.entity.Account;
import fmalc.api.entity.Driver;
import fmalc.api.entity.FleetManager;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.FleetManagerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static fmalc.api.constant.SecurityConstant.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private FleetManagerRepository fleetManagerRepository;

    private DriverRepository driverRepository;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, FleetManagerRepository fleetManagerRepository, DriverRepository driverRepository) {
        this.authenticationManager = authenticationManager;
        this.fleetManagerRepository = fleetManagerRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {

            Account creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Account.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        List<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Algorithm alg = Algorithm.HMAC256(SECRET);
        String[] tmp = new String[roles.size()];
        roles.toArray(tmp);
        String token = JWT.create()
                .withClaim("username", ((UserDetails) auth.getPrincipal()).getUsername())
                .withArrayClaim("roles", tmp)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).sign(alg);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.addHeader("Access-Control-Expose-Headers", "Access-Token, Uid");
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setUsername(username);
        loginResponseDTO.setToken(TOKEN_PREFIX + token);
        loginResponseDTO.setRole(roles.get(0));
        if (roles.get(0).equals("ROLE_FLEET_MANAGER")) {
            FleetManager fleetManager = fleetManagerRepository.findByAccount_Username(username);
            loginResponseDTO.setAvatar(fleetManager.getImage());
            loginResponseDTO.setName(fleetManager.getName());
            loginResponseDTO.setId(fleetManager.getId());
        } else if (roles.get(0).equals("ROLE_DRIVER")) {
            Driver driver = driverRepository.findByAccount_Username(username);
            loginResponseDTO.setId(driver.getId());
        }
        Gson gson = new Gson();
        res.getWriter().write(gson.toJson(loginResponseDTO));
    }
}