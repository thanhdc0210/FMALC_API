package fmalc.api.service.impl;

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
//        location.setId(null);
        locationRepository.save(location);
        return location;
    }

    @Override
    public List<Location> getListLocationBySchedule(int scheduleId) {
        return locationRepository.findBySchedule(scheduleId);
    }

//    @Override
//    public List<Location> getListLocationByVehicle(int vehicle) {
//
//        return locationRepository.getListLocationById(vehicle);
//    }
}
