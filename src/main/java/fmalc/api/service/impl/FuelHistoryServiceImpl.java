package fmalc.api.service.impl;

import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.FuelHistory;
import fmalc.api.entity.Vehicle;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.FuelHistoryRepository;
import fmalc.api.repository.FuelTypeRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.FuelHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class FuelHistoryServiceImpl implements FuelHistoryService {

    @Autowired
    FuelTypeRepository fuelTypeRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    FuelHistoryRepository fuelHistoryRepository;
    @Autowired
    DriverRepository driverRepository;

    @Override
    public FuelHistory saveFuelFilling(FuelRequestDTO fuelTypeRequestDTO) {
        FuelHistory fuelHistory = new FuelHistory();
        fuelHistory.setFillingDate(new Date(System.currentTimeMillis()));
        fuelHistory.setFuelType(fuelTypeRepository.findById(fuelTypeRequestDTO.getFuelTypeId()).get());
        fuelHistory.setKmOld(fuelTypeRequestDTO.getKmOld());
        fuelHistory.setUnitPriceAtFillingTime(fuelTypeRequestDTO.getUnitPriceAtFillingTime());
        fuelHistory.setVolume(fuelTypeRequestDTO.getVolume());
        Vehicle vehicle = vehicleRepository.findByLicensePlates(fuelTypeRequestDTO.getVehicleLicensePlates());
        vehicle.setKilometerRunning(fuelTypeRequestDTO.getKmOld());
        fuelHistory.setVehicle(vehicle);
        fuelHistory.setDriver(driverRepository.findById(fuelTypeRequestDTO.getDriverId()).get());
        return fuelHistoryRepository.save(fuelHistory);
    }

    @Override
    public List<FuelHistory> getListFuelByVehicleId(int idVehicle) {
        return fuelHistoryRepository.getByVehicle(idVehicle);
    }
}
