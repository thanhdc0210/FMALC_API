package fmalc.api.service;

import fmalc.api.entity.Location;

import java.util.List;

public interface LocationService {
    Location createLocation(Location location);
    List<Location> getListLocationBySchedule(int scheduleId);
//    List<LocationDTO> getListLocationByVehicle(int vehicle);
}
