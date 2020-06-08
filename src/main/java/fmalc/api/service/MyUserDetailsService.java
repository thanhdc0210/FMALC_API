package fmalc.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    // @Autowired
    // private AccountRepository accountRepository;
    //
    // @Override
    // public UserDetails loadUserByUsername(String username)
    // throws UsernameNotFoundException {
    //
    // Account user = accountRepository.findByUsername(username);
    // if (user == null) {
    // return new org.springframework.security.core.userdetails.User(
    // " ", " ", true, true, true, true,
    // getAuthorities("USER"));
    // }
    //
    // return new org.springframework.security.core.userdetails.User(
    // user.getUsername(), user.getPassword(), true, true, true,
    // true, getAuthorities(user.getRole()));
    // }
    //
    // private Collection<? extends GrantedAuthority> getAuthorities(String role) {
    // List<GrantedAuthority> authorities = new ArrayList<>();
    // authorities.add(new SimpleGrantedAuthority(role));
    // return authorities;
    // }
}