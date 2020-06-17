package fmalc.api.service;


import fmalc.api.entity.VehicleType;

import java.util.List;

public interface VehicleTypeService {
    VehicleType saveTypeVehicle(VehicleType vehicleType);
    List<VehicleType> getListVehicleType();
}
