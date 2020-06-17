package fmalc.api.service;

import fmalc.api.entities.DriverLicense;

import java.util.List;

public interface DriverLicenseService {
    DriverLicense createLicense(DriverLicense driverLicense);
    List<DriverLicense> getListDriverLicense();
}
