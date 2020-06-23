package fmalc.api.service.impl;


import fmalc.api.dto.VehicleTypeDTO;
import fmalc.api.entity.VehicleType;
import fmalc.api.repository.VehicleTypeRepository;
import fmalc.api.service.VehicleTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleTypeServiceImpl implements VehicleTypeService {

    @Autowired
    VehicleTypeRepository vehicleTypeRepository;

    @Override
    public VehicleType saveTypeVehicle(VehicleType vehicleType) {
        return vehicleTypeRepository.save(vehicleType);
    }

    @Override
    public VehicleType getTypeByLicense(double weight) {
        return vehicleTypeRepository.findByWeight(weight);
    }

    @Override
    public List<VehicleTypeDTO> getListVehicleType() {
        List<VehicleType> vehicleTypes = vehicleTypeRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();
        return vehicleTypes.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    private VehicleTypeDTO convertToDto(VehicleType vehicleType) {
        ModelMapper modelMapper = new ModelMapper();
        VehicleTypeDTO dto = modelMapper.map(vehicleType, VehicleTypeDTO.class);

        return dto;
    }
}
