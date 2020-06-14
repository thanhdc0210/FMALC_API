package fmalc.api.service;

import fmalc.api.dto.DriverRequestDTO;
import fmalc.api.entities.Driver;

import java.util.List;

public interface DriverService {
    List<Driver> findAll();

    Driver findById(Integer id);

    Driver save(DriverRequestDTO driverRequest);
}
