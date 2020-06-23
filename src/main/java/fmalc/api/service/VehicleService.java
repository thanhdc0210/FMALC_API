package fmalc.api.service;


import fmalc.api.dto.VehicleForDetailDTO;
import fmalc.api.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle saveVehicle (Vehicle vehicle);
    VehicleForDetailDTO findVehicleById(int id);
    Vehicle findVehicleByIdForLocation(int id);
    List<Vehicle> getListVehicle();
    Vehicle findVehicleByLicensePlates (String licensePlates);
    Vehicle findByStatus(int status);
}
