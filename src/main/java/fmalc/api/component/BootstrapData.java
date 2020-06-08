package fmalc.api.component;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

    }

    // @Autowired
    // private AccountRepository accountRepository;
    //
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    //
    // @Override
    // @Transactional
    // public void onApplicationEvent(ContextRefreshedEvent event) {
    // Account admin = accountRepository.findByUsername("admin");
    // if (admin == null) {
    // admin = new Account();
    // admin.setUsername("admin");
    // admin.setPassword(passwordEncoder.encode("123456"));
    // admin.setRole("FLEET_MANAGER");
    // accountRepository.save(admin);
    // }
    // }
}
