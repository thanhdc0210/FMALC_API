package fmalc.api.service;

import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.RefuelHistory;

import java.util.List;

public interface RefuelHistoryService {
    RefuelHistory saveFuelFilling(FuelRequestDTO fuelTypeRequestDTO);
    List<RefuelHistory> getListFuelByVehicleId(int idVehicle);
}
