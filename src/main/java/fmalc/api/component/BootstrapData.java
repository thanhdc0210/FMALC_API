package fmalc.api.component;

import fmalc.api.entity.Account;
import fmalc.api.entity.Role;
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
        // Check role FLEET_ADMIN exist
        Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new Role();
            roleAdmin.setRole("ROLE_ADMIN");
            roleAdmin = roleRepository.save(roleAdmin);
        }

        // Check account FLEET_ADMIN exist
        Account roleAccount = accountRepository.findByUsername("admin");
        if (roleAccount == null) {
            roleAccount = new Account();
            roleAccount.setUsername("admin");
            roleAccount.setPassword(passwordEncoder.encode("123456"));
            roleAccount.setRole(roleAdmin);
            roleAccount.setIsActive(true);
            accountRepository.save(roleAccount);
        }
    }
}
