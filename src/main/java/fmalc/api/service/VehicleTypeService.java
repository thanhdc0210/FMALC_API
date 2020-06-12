package fmalc.api.service;

import fmalc.api.entities.VehicleType;

import java.util.List;

public interface VehicleTypeService {
    VehicleType saveTypeVehicle(VehicleType vehicleType);
    List<VehicleType> getListVehicleType();
}
