package fmalc.api.service;

import fmalc.api.entities.Driver;
import fmalc.api.request.DriverRequest;

import java.util.List;

public interface DriverService {
    List<Driver> findAll();

    Driver findById(Integer id);

    Driver save(DriverRequest driverRequest);

    void changeStatus(Driver driver, Integer status);
}
