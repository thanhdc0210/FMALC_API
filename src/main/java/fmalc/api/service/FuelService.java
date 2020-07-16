package fmalc.api.service;

import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.Fuel;

public interface FuelService {
    Fuel saveFuelFilling(FuelRequestDTO fuelTypeRequestDTO);
}
