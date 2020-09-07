package fmalc.api.service.impl;

import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.Fuel;
import fmalc.api.entity.FuelType;
import fmalc.api.entity.Vehicle;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.FuelRepository;
import fmalc.api.repository.FuelTypeRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.FuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class FuelServiceImpl implements FuelService {

    @Autowired
    FuelTypeRepository fuelTypeRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    FuelRepository fuelRepository;
    @Autowired
    DriverRepository driverRepository;

    @Override
    public Fuel saveFuelFilling(FuelRequestDTO fuelTypeRequestDTO) {
        Fuel fuel = new Fuel();
        fuel.setFillingDate(new Date(System.currentTimeMillis()));
        fuel.setFuelType(fuelTypeRepository.findById(fuelTypeRequestDTO.getFuelTypeId()).get());
        fuel.setKmOld(fuelTypeRequestDTO.getKmOld());
        fuel.setUnitPriceAtFillingTime(fuelTypeRequestDTO.getUnitPriceAtFillingTime());
        fuel.setVolume(fuelTypeRequestDTO.getVolume());
        Vehicle vehicle = vehicleRepository.findByLicensePlates(fuelTypeRequestDTO.getVehicleLicensePlates());
        vehicle.setKilometerRunning(fuelTypeRequestDTO.getKmOld());
        fuel.setVehicle(vehicle);
        fuel.setDriver(driverRepository.findById(fuelTypeRequestDTO.getDriverId()).get());
        return fuelRepository.save(fuel);
    }

    @Override
    public List<Fuel> getListFuelByVehicleId(int idVehicle) {
        return fuelRepository.getByVehicle(idVehicle);
    }
}
