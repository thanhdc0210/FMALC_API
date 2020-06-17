package fmalc.api.service;

import fmalc.api.dto.LocationDTO;

import fmalc.api.entity.Location;

import java.util.List;

public interface LocationService {
    Location createLocation(Location location);
    List<Location> getListLocationByVehicle(int vehicle);
//    List<LocationDTO> getListLocationByVehicle(int vehicle);
}
