package fmalc.api.service.impl;

import fmalc.api.dto.FleetManagerRequestDTO;
import fmalc.api.entity.Account;
import fmalc.api.entity.FleetManager;
import fmalc.api.entity.Role;
import fmalc.api.repository.AccountRepository;
import fmalc.api.repository.FleetManagerRepository;
import fmalc.api.repository.RoleRepository;
import fmalc.api.service.FleetManagerService;
import fmalc.api.service.UploaderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FleetManagerServiceImpl implements FleetManagerService {

    @Autowired
    FleetManagerRepository fleetManagerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UploaderService uploaderService;

    @Override
    public List<FleetManager> getAllFleet() {
        return fleetManagerRepository.findAll();
    }

    @Override
    public FleetManager save(FleetManagerRequestDTO fleetManagerRequestDTO, MultipartFile file) throws IOException {
        ModelMapper modelMapper = new ModelMapper();
        FleetManager fleetManager = modelMapper.map(fleetManagerRequestDTO, FleetManager.class);

        Role role = roleRepository.findByRole("ROLE_FLEET_MANAGER");

        Account account = new Account();
        account.setUsername(fleetManager.getPhoneNumber());
        // To do random password
        account.setPassword(passwordEncoder.encode("123456"));
        account.setRole(role);
        account.setIsActive(true);
        account = accountRepository.save(account);

        fleetManager.setId(null);
        fleetManager.setAccount(account);
        String link = uploaderService.upload(file);
        fleetManager.setImage(link);
        fleetManagerRepository.save(fleetManager);

        return fleetManager;
    }

    @Override
    public FleetManager findById(Integer id) {
        if (fleetManagerRepository.existsById(id)) {
            return fleetManagerRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public FleetManager update(Integer id, FleetManagerRequestDTO fleetManagerRequestDTO) {
        fleetManagerRepository.updateFleetManager(id, fleetManagerRequestDTO.getName(), fleetManagerRequestDTO.getIdentityNo());
        return fleetManagerRepository.findById(id).get();
    }

}
