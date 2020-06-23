//package fmalc.api.component;
//
//import fmalc.api.entity.*;
//import fmalc.api.repository.AccountRepository;
//import fmalc.api.repository.FleetManagerRepository;
//import fmalc.api.repository.RoleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class BootstrapData implements ApplicationListener<ContextRefreshedEvent> {
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private FleetManagerRepository fleetManagerRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private DriverLicenseRepository driverLicenseRepository;
//
//    @Override
//    @Transactional
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        // Check role FLEET_ADMIN exist
//        Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN");
//        if (roleAdmin == null) {
//            roleAdmin = new Role();
//            roleAdmin.setRole("ROLE_ADMIN");
//            roleAdmin = roleRepository.save(roleAdmin);
//        }
//
//        // Check role FLEET_MANAGER exist
//        Role roleFleetManager = roleRepository.findByRole("ROLE_FLEET_MANAGER");
//        if (roleFleetManager == null) {
//            roleFleetManager = new Role();
//            roleFleetManager.setRole("ROLE_FLEET_MANAGER");
//            roleRepository.save(roleFleetManager);
//        }
//
//        // Check role DRIVER exist
//        Role roleDriver = roleRepository.findByRole("ROLE_DRIVER");
//        if (roleDriver == null) {
//            roleDriver = new Role();
//            roleDriver.setRole("ROLE_DRIVER");
//            roleRepository.save(roleDriver);
//        }
//
//        // Check account ADMIN exist
//        Account adminAccount = accountRepository.findByUsername("admin");
//        if (adminAccount == null) {
//            adminAccount = new Account();
//            adminAccount.setUsername("admin");
//            adminAccount.setPassword(passwordEncoder.encode("123456"));
//            adminAccount.setRole(roleAdmin);
//            adminAccount.setIsActive(true);
//            accountRepository.save(adminAccount);
//        }
//
//        // Account for first Fleet_Manager to test
//        Account managerAccount = accountRepository.findByUsername("manager1");
//        if (managerAccount == null) {
//            managerAccount = new Account();
//            managerAccount.setUsername("manager1");
//            managerAccount.setPassword(passwordEncoder.encode("123456"));
//            managerAccount.setRole(roleFleetManager);
//            managerAccount.setIsActive(true);
//            managerAccount = accountRepository.save(managerAccount);
//
//            FleetManager fleetManager = new FleetManager();
//            fleetManager.setAccount(managerAccount);
//            fleetManager.setIdentityNo("123456789");
//            fleetManager.setName("Fleet Manager");
//            fleetManager.setPhoneNumber("0909090909");
//            fleetManagerRepository.save(fleetManager);
//        }
//
//        List<String> licenseTypes = Arrays.asList("B2", "C", "D", "E");
//        List<String> driverLicenses = driverLicenseRepository.findAll().stream().map(x -> x.getLicenseType()).collect(Collectors.toList());
//        List<String> notExists = licenseTypes.stream().filter(x -> !driverLicenses.contains(x))
//                .collect(Collectors.toList());
//        List<DriverLicense> lst = new ArrayList<>();
//        for (String type: notExists) {
//            DriverLicense driverLicense = new DriverLicense();
//            driverLicense.setLicenseType(type);
//            lst.add(driverLicense);
//        }
//        driverLicenseRepository.saveAll(lst);
//    }
//}
