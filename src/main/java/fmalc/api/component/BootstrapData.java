package fmalc.api.component;

import fmalc.api.entity.*;
import fmalc.api.repository.AccountRepository;
import fmalc.api.repository.FleetManagerRepository;
import fmalc.api.repository.MaintenanceTypeRepository;
import fmalc.api.repository.RoleRepository;
import fmalc.api.util.FuelTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Timer;

@Component
public class BootstrapData implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FleetManagerRepository fleetManagerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MaintenanceTypeRepository maintenanceTypeRepository;

    @Autowired
    private FuelTypeUtil fuelTypeUtil;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        bootstrapAccount();
        bootstrapMaintainType();
        cronJob();
    }

    private void bootstrapAccount() {
        // Check role FLEET_ADMIN exist
        Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new Role();
            roleAdmin.setRole("ROLE_ADMIN");
            roleAdmin = roleRepository.save(roleAdmin);
        }

        // Check role FLEET_MANAGER exist
        Role roleFleetManager = roleRepository.findByRole("ROLE_FLEET_MANAGER");
        if (roleFleetManager == null) {
            roleFleetManager = new Role();
            roleFleetManager.setRole("ROLE_FLEET_MANAGER");
            roleRepository.save(roleFleetManager);
        }

        // Check role DRIVER exist
        Role roleDriver = roleRepository.findByRole("ROLE_DRIVER");
        if (roleDriver == null) {
            roleDriver = new Role();
            roleDriver.setRole("ROLE_DRIVER");
            roleRepository.save(roleDriver);
        }

        // Check account ADMIN exist
        Account adminAccount = accountRepository.findByUsername("admin");
        if (adminAccount == null) {
            adminAccount = new Account();
            adminAccount.setUsername("admin");
            adminAccount.setPassword(passwordEncoder.encode("123456"));
            adminAccount.setRole(roleAdmin);
            adminAccount.setIsActive(true);
            accountRepository.save(adminAccount);
        }

        // Account for first Fleet_Manager to test
        Account managerAccount = accountRepository.findByUsername("manager1");
        if (managerAccount == null) {
            managerAccount = new Account();
            managerAccount.setUsername("manager1");
            managerAccount.setPassword(passwordEncoder.encode("123456"));
            managerAccount.setRole(roleFleetManager);
            managerAccount.setIsActive(true);
            managerAccount = accountRepository.save(managerAccount);

            FleetManager fleetManager = new FleetManager();
            fleetManager.setAccount(managerAccount);
            fleetManager.setIdentityNo("123456789");
            fleetManager.setName("Fleet Manager Default");
            fleetManager.setPhoneNumber("0909090909");
            fleetManager.setImage("https://fmalc-img.s3-ap-southeast-1.amazonaws.com/abc.jpg");
            fleetManagerRepository.save(fleetManager);
        }
    }

    private void bootstrapMaintainType() {
        MaintenanceType maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 1");
        if (maintenanceType == null) {
            maintenanceType = new MaintenanceType();
            maintenanceType.setMaintenanceTypeName("Loại 1");
            maintenanceType.setContent("Thay nhớt, kiểm tra lốp, kiểm tra thắng,...");
            maintenanceType.setKilometersNumber(5000);
            maintenanceTypeRepository.save(maintenanceType);
        }
        maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 2");
        if (maintenanceType == null) {
            maintenanceType = new MaintenanceType();
            maintenanceType.setMaintenanceTypeName("Loại 2");
            maintenanceType.setContent("Đảo lốp, thay nhớt, kiểm tra lốp, kiểm tra thắng,...");
            maintenanceType.setKilometersNumber(10000);
            maintenanceTypeRepository.save(maintenanceType);
        }
        maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 3");
        if (maintenanceType == null) {
            maintenanceType = new MaintenanceType();
            maintenanceType.setMaintenanceTypeName("Loại 3");
            maintenanceType.setContent("kiểm tra các chi tiết như dầu máy, lọc gió động cơ, lọc gió điều hòa, mực dầu thắng, dầu hộp số, nước làm mát, nước rửa kính....");
            maintenanceType.setKilometersNumber(5000);
            maintenanceTypeRepository.save(maintenanceType);
        }
    }

    private void cronJob() {
        Timer t = new Timer();

        // This task is scheduled to run every hour
        t.scheduleAtFixedRate(fuelTypeUtil, 0, 1000 * 60 * 60);
    }
}
