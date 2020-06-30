

package fmalc.api.service.impl;

import fmalc.api.dto.DriverRequestDTO;
import fmalc.api.entity.*;
import fmalc.api.enums.DriverLicenseEnum;
import fmalc.api.repository.*;
import fmalc.api.service.DriverService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
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
//<<<<<<< HEAD
//        DriverLicenseRequestDTO driverLicenseRequest = driverRequest.getDriverLicenseRequestDTO();
////        DriverLicense driverLicense = modelMapper.map(driverLicenseRequest, DriverLicense.class);
//=======
//>>>>>>> a2a2f9eb85853370ceda5fb37ef5ca1ec5351d75
        Role role = roleRepository.findByRole("ROLE_DRIVER");

        Account account = new Account();
        account.setUsername(driver.getPhoneNumber());
        // To do random password
        account.setPassword(passwordEncoder.encode("123456"));
        account.setRole(role);
        account.setIsActive(true);
        account = accountRepository.save(account);

        FleetManager fleetManager = fleetManagerRepository.findById(driverRequest.getFleetManagerId()).get();






        driver.setAccount(account);

//        driverLicense = driverLicenseRepository.save(driverLicense);
//        driver.setLicense(driverLicense);
        driver.setFleetManager(fleetManager);
        driverRepository.save(driver);

        return driver;
    }


//=======
//        driver.setId(null);
//        driver.setAccount(account);
//        driver.setFleetManager(fleetManager);
//        driverRepository.save(driver);
//        return driver;



    @Override
    public Driver update(Integer id, DriverRequestDTO driverRequest) throws Exception {
        if (!driverRepository.existsById(id)) {
            throw new Exception();
        }

        Driver driverUpdate = driverRepository.findById(id).get();


        driverUpdate.setName(driverRequest.getName());
        driverUpdate.setIdentityNo(driverRequest.getIdentityNo());
        driverUpdate.setNo(driverRequest.getNo());
        driverUpdate.setLicense_expires(driverRequest.getLicense_expires());

//        driverUpdate.setLicense(driverLicenseUpdate);
        driverRepository.save(driverUpdate);
        return driverUpdate;
    }

    @Override
    public List<Driver> getListDriverByLicense(double weight) {
        List<Driver> drivers = new ArrayList<>();
        if(weight >3.5){
            drivers = driverRepository.findByDriverLicenseC(DriverLicenseEnum.C.getValue());
        }else{
            drivers = driverRepository.findByDriverLicenseB2(DriverLicenseEnum.B2.getValue());
        }

        return drivers;
    }

}



