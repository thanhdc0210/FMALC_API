package fmalc.api.service.impl;

import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.RefuelHistory;
import fmalc.api.entity.Vehicle;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.RefuelHistoryRepository;
import fmalc.api.repository.FuelTypeRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.RefuelHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class RefuelHistoryServiceImpl implements RefuelHistoryService {

    @Autowired
    FuelTypeRepository fuelTypeRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    RefuelHistoryRepository refuelHistoryRepository;
    @Autowired
    DriverRepository driverRepository;

    @Override
    public RefuelHistory saveFuelFilling(FuelRequestDTO fuelTypeRequestDTO) {
        RefuelHistory refuelHistory = new RefuelHistory();
        refuelHistory.setFillingDate(new Date(System.currentTimeMillis()));
        refuelHistory.setFuelType(fuelTypeRepository.findById(fuelTypeRequestDTO.getFuelTypeId()).get());
        refuelHistory.setKmOld(fuelTypeRequestDTO.getKmOld());
        refuelHistory.setUnitPriceAtFillingTime(fuelTypeRequestDTO.getUnitPriceAtFillingTime());
        refuelHistory.setVolume(fuelTypeRequestDTO.getVolume());
        Vehicle vehicle = vehicleRepository.findByLicensePlates(fuelTypeRequestDTO.getVehicleLicensePlates());
        vehicle.setKilometerRunning(fuelTypeRequestDTO.getKmOld());
        refuelHistory.setVehicle(vehicle);
        refuelHistory.setDriver(driverRepository.findById(fuelTypeRequestDTO.getDriverId()).get());
        return refuelHistoryRepository.save(refuelHistory);
    }

    @Override
    public List<RefuelHistory> getListFuelByVehicleId(int idVehicle) {
        return refuelHistoryRepository.getByVehicle(idVehicle);
    }
}
