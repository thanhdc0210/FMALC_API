package fmalc.api.service;

import fmalc.api.entities.Driver;
import fmalc.api.model.DriverDTO;

import java.util.List;

public interface DriverService {
    List<Driver> findAll();

    Driver findById(Integer id);

    void save(DriverDTO driverDTO);

    void changeStatus(Driver driver, Integer status);
}
