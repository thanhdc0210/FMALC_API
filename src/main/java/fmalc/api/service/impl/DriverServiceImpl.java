package fmalc.api.service.impl;

import fmalc.api.dto.DriverLicenseRequestDTO;
import fmalc.api.dto.DriverRequestDTO;
import fmalc.api.entity.*;
import fmalc.api.repository.*;
import fmalc.api.service.DriverService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
    @Autowired
    private DriverRepository driverRepository;

    @Override
    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DriverLicenseRepository driverLicenseRepository;

    @Autowired
    private FleetManagerRepository fleetManagerRepository;

    @Override
    public Driver findById(Integer id) {
        if (driverRepository.existsById(id)) {
            return driverRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public Driver save(DriverRequestDTO driverRequest) {
        ModelMapper modelMapper = new ModelMapper();
        Driver driver = modelMapper.map(driverRequest, Driver.class);
        DriverLicenseRequestDTO driverLicenseRequest = driverRequest.getDriverLicenseRequestDTO();
        DriverLicense driverLicense = modelMapper.map(driverLicenseRequest, DriverLicense.class);
        Role role = roleRepository.findByRole("ROLE_DRIVER");

        Account account = new Account();
        account.setUsername(driver.getPhoneNumber());
        // To do random password
        account.setPassword(passwordEncoder.encode("123456"));
        account.setRole(role);
        account.setIsActive(true);
        account = accountRepository.save(account);

        FleetManager fleetManager = fleetManagerRepository.findById(driverRequest.getFleetManagerId()).get();

        driver.setId(null);
        driver.setAccount(account);
        driverLicense = driverLicenseRepository.save(driverLicense);
        driver.setLicense(driverLicense);
        driver.setFleetManager(fleetManager);
        driverRepository.save(driver);
        return driver;
    }
}
