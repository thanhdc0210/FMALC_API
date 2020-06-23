package fmalc.api.service;

import fmalc.api.dto.DriverRequestDTO;
import fmalc.api.entity.Driver;

import java.util.List;

public interface DriverService {
    List<Driver> findAll();

    Driver findById(Integer id);

    Driver save(DriverRequestDTO driverRequest);


    Driver update(Integer id, DriverRequestDTO driverRequest) throws Exception;

}
