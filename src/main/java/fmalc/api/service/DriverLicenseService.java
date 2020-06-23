package fmalc.api.service;


import fmalc.api.entity.DriverLicense;

import java.util.List;

public interface DriverLicenseService {
    DriverLicense createLicense(DriverLicense driverLicense);
    List<DriverLicense> getListDriverLicense();
    DriverLicense getLicenseByLicenseType(String type);
}
