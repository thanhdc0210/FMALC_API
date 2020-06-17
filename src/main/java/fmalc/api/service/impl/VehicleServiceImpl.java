package fmalc.api.service.impl;


import fmalc.api.entity.Vehicle;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
        return vehicle;
    }

    @Override
    public Vehicle findVihicleById(int id) {
        Vehicle vehicle =   vehicleRepository.findByIdVehicle(id);
        return vehicle;
    }

    @Override
    public List<Vehicle> getListVehicle() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles = vehicleRepository.findAll();

        return vehicles;
    }
}
