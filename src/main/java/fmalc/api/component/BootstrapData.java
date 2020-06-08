package fmalc.api.component;

import fmalc.api.entities.Account;
import fmalc.api.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BootstrapData implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        Account admin = accountRepository.findByUsername("admin");
//        if (admin == null) {
//            admin = new Account();
//            admin.setUsername("admin");
//            admin.setPassword(passwordEncoder.encode("123456"));
//            admin.setRole("FLEET_MANAGER");
//            accountRepository.save(admin);
//        }
//    }
}
