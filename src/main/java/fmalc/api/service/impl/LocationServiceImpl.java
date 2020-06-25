package fmalc.api.service.impl;

import fmalc.api.dto.LocationDTO;

import fmalc.api.entity.Location;
import fmalc.api.repository.LocationRepository;
import fmalc.api.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepository locationRepository;



    @Override
    public Location createLocation(Location location) {
        locationRepository.save(location);
        return location;
    }

    @Override
    public List<Location> getListLocationByVehicle(int vehicle) {

        return locationRepository.getListLocationById(vehicle);
    }
}
