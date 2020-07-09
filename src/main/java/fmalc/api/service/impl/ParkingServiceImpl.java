package fmalc.api.service.impl;

import fmalc.api.dto.ParkingDTO;
import fmalc.api.service.ParkingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ParkingServiceImpl implements ParkingService {
    @Value("${location.longitude}")
    private double longitude;
    @Value("${location.latitude}")
    private double latitude;
    @Value("${location.address}")
    private String address;


    @Override
    public ParkingDTO getParking() {
        ParkingDTO parkingDTO = new ParkingDTO();
        parkingDTO.setAddress(address);
        parkingDTO.setLatitude(latitude);
        parkingDTO.setLongitude(longitude);
        return parkingDTO;
    }
}
