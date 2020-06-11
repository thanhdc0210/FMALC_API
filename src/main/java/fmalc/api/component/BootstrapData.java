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
        Role fleetManagerRole = roleRepository.findByRole("ROLE_ADMIN");
        if (fleetManagerRole == null) {
            fleetManagerRole = new Role();
            fleetManagerRole.setRole("ROLE_ADMIN");
            fleetManagerRole = roleRepository.save(fleetManagerRole);
        }

        // Check role DRIVER exist
        Role driverRole = roleRepository.findByRole("ROLE_DRIVER");
        if (driverRole == null) {
            driverRole = new Role();
            driverRole.setRole("ROLE_DRIVER");
            roleRepository.save(driverRole);
        }

        // Check account FLEET_MANAGER exist
        Account fleetManagerAccount = accountRepository.findByUsername("admin");
        if (fleetManagerAccount == null) {
            fleetManagerAccount = new Account();
            fleetManagerAccount.setUsername("admin");
            fleetManagerAccount.setPassword(passwordEncoder.encode("123456"));
            fleetManagerAccount.setRole(fleetManagerRole);
            accountRepository.save(fleetManagerAccount);
        }
    }
}
