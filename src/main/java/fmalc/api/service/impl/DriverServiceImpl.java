

package fmalc.api.service.impl;

import fmalc.api.dto.DriverRequestDTO;
import fmalc.api.entity.*;
import fmalc.api.enums.DriverLicenseEnum;
import fmalc.api.repository.*;
import fmalc.api.service.DriverService;
import fmalc.api.service.UploaderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
    @Autowired
    private DriverRepository driverRepository;

    @Override
    public List<Driver> findAllAndSearch(String searchPhone) {
        return driverRepository.findByPhoneNumberContainingIgnoreCase(searchPhone);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private UploaderService uploaderService;


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
    public Driver save(DriverRequestDTO driverRequest, MultipartFile file) throws IOException {
        ModelMapper modelMapper = new ModelMapper();
        Driver driver = modelMapper.map(driverRequest, Driver.class);
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
        String link = uploaderService.upload(file);
        driver.setImage(link);
        driver.setWorkingHour(0f);
        driver.setFleetManager(fleetManager);
        driverRepository.save(driver);

        return driver;
    }

    @Override
    public Driver update(Integer id, DriverRequestDTO driverRequest) throws Exception {
        if (!driverRepository.existsById(id)) {
            throw new Exception();
        }
        driverRepository.updateDriver(id,driverRequest.getName(), driverRequest.getIdentityNo(), driverRequest.getNo(), driverRequest.getLicenseExpires(), driverRequest.getDateOfBirth());
        return driverRepository.findById(id).get();
    }

    @Override
    public List<Driver> findDriverByLicense(double weight) {
        List<Driver> drivers = new ArrayList<>();
        if(weight >3.5){
            drivers = driverRepository.findDriverByLicenseC(DriverLicenseEnum.C.getValue());
        }else{
            drivers = driverRepository.findDriverByLicenseB2(DriverLicenseEnum.B2.getValue());
        }
        return drivers;
    }

    @Override
    public List<Driver> getListDriverByLicense(double weight, int status) {
        List<Driver> drivers = new ArrayList<>();
        if(weight >3.5){
            drivers = driverRepository.findByDriverLicenseC(DriverLicenseEnum.C.getValue(), status);
        }else{
            drivers = driverRepository.findByDriverLicenseB2(DriverLicenseEnum.B2.getValue(), status);
        }
        return drivers;
    }

    @Override
    public int updateStatus(int status, int id) {
        return driverRepository.updateStatusDriver(status,id);
    }

    @Override
    public Driver updateAvatar(Integer id, MultipartFile file) throws IOException {
        String image = uploaderService.upload(file);
        driverRepository.updateImageById(id, image);
        return driverRepository.findById(id).get();
    }

    @Override
    public Driver findDriverByUsername(String username) {

        return driverRepository.findDriverByUsername(username);
    }

}



