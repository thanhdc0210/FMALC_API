package fmalc.api.service;


import fmalc.api.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle saveVehicle (Vehicle vehicle);
    Vehicle findVihicleById(int id);
    List<Vehicle> getListVehicle();
}
