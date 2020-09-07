package fmalc.api.service;

import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.FuelHistory;

import java.util.List;

public interface FuelHistoryService {
    FuelHistory saveFuelFilling(FuelRequestDTO fuelTypeRequestDTO);
    List<FuelHistory> getListFuelByVehicleId(int idVehicle);
}
