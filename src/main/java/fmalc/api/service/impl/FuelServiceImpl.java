package fmalc.api.service.impl;

import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.Fuel;
import fmalc.api.repository.FuelRepository;
import fmalc.api.repository.FuelTypeRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.FuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class FuelServiceImpl implements FuelService {

    @Autowired
    FuelTypeRepository fuelTypeRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    FuelRepository fuelRepository;

    @Override
    public Fuel saveFuelFilling(FuelRequestDTO fuelTypeRequestDTO) {
        Fuel fuel = new Fuel();
        fuel.setFillingDate(new Date(System.currentTimeMillis()));
        fuel.setFuelType(fuelTypeRepository.findById(fuelTypeRequestDTO.getFuelTypeId()).get());
        fuel.setKmOld(fuelTypeRequestDTO.getKmOld());
        fuel.setUnitPriceAtFillingTime(fuelTypeRequestDTO.getUnitPriceAtFillingTime());
        fuel.setVolume(fuelTypeRequestDTO.getVolumne());
        fuel.setVehicle(vehicleRepository.findByLicensePlates(fuelTypeRequestDTO.getVehicleLiencePlates()));
        return fuelRepository.save(fuel);
    }
}
