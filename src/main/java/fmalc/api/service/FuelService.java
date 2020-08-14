package fmalc.api.service;

import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.Fuel;
import fmalc.api.entity.FuelType;

import java.util.List;

public interface FuelService {
    Fuel saveFuelFilling(FuelRequestDTO fuelTypeRequestDTO);
    List<Fuel> getListFuelByVehicleId(int idVehicle);
}
