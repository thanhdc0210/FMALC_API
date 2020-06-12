package fmalc.api.service.impl;

import fmalc.api.entities.Account;
import fmalc.api.entities.Driver;
import fmalc.api.entities.DriverLicense;
import fmalc.api.entities.Role;
import fmalc.api.model.DriverDTO;
import fmalc.api.model.DriverLicenseDTO;
import fmalc.api.repository.AccountRepository;
import fmalc.api.repository.DriverLicenseRepository;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.RoleRepository;
import fmalc.api.service.DriverService;
import io.swagger.models.License;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    ModelMapper modelMapper;

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

    @Override
    public Driver findById(Integer id) {
        if (driverRepository.existsById(id)) {
            return driverRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public void save(DriverDTO driverDTO) {
        Driver driver = modelMapper.map(driverDTO, Driver.class);
        DriverLicenseDTO driverLicenseDTO = driverDTO.getLicenseDTO();
        DriverLicense driverLicense = modelMapper.map(driverLicenseDTO, DriverLicense.class);
        Role role = roleRepository.findByRole("ROLE_DRIVER");

        Account account = new Account();
        account.setUsername(driver.getPhoneNumber());
        account.setPassword(passwordEncoder.encode("123456"));
        account.setRole(role);
        account = accountRepository.save(account);

        driver.setAccountId(account);
        driverLicense = driverLicenseRepository.save(driverLicense);
        driver.setLicense(driverLicense);
        driverRepository.save(driver);
    }

    @Override
    public void changeStatus(Driver driver, Integer status) {
        driver.setStatus(status);
        driverRepository.save((driver));
    }
}
