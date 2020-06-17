package fmalc.api.service.impl;

import fmalc.api.entities.DriverLicense;
import fmalc.api.repository.DriverLicenseRepository;
import fmalc.api.service.DriverLicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverLicenseServiceImpl implements DriverLicenseService {

    @Autowired
    DriverLicenseRepository driverLicenseRepository;

    @Override
    public DriverLicense createLicense(DriverLicense driverLicense) {
        return driverLicenseRepository.save(driverLicense);
    }

    @Override
    public List<DriverLicense> getListDriverLicense() {
        return driverLicenseRepository.findAll();
    }
}
