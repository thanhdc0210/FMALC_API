package fmalc.api.service;

import org.springframework.stereotype.Service;

import fmalc.api.dto.ParkingDTO;

public interface ParkingService {
    ParkingDTO getParking();
}
