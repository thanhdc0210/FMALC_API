package fmalc.api.service.impl;


import fmalc.api.entity.VehicleType;
import fmalc.api.repository.VehicleTypeRepository;
import fmalc.api.service.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleTypeServiceImpl implements VehicleTypeService {

    @Autowired
    VehicleTypeRepository vehicleTypeRepository;

    @Override
    public VehicleType saveTypeVehicle(VehicleType vehicleType) {
        return vehicleTypeRepository.save(vehicleType);
    }

    @Override
    public List<VehicleType> getListVehicleType() {
        return vehicleTypeRepository.findAll();
    }
}
