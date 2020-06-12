package fmalc.api.component;

import fmalc.api.entities.Account;
import fmalc.api.entities.Role;
import fmalc.api.repository.AccountRepository;
import fmalc.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BootstrapData implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Check role FLEET_MANAGER exist
        Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new Role();
            roleAdmin.setRole("ROLE_ADMIN");
            roleAdmin = roleRepository.save(roleAdmin);
        }

        // Check role DRIVER exist
        Role driverRole = roleRepository.findByRole("ROLE_DRIVER");
        if (driverRole == null) {
            driverRole = new Role();
            driverRole.setRole("ROLE_DRIVER");
            roleRepository.save(driverRole);
        }

        // Check account FLEET_MANAGER exist
        Account admin = accountRepository.findByUsername("admin");
        if (admin == null) {
            admin = new Account();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole(roleAdmin);
            accountRepository.save(admin);
        }
    }
}
