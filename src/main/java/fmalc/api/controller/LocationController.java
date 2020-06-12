package fmalc.api.controller;

import fmalc.api.dto.LocationDTO;
import fmalc.api.entities.Location;
import fmalc.api.entities.Vehicle;
import fmalc.api.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping("/sendLocation")
    public ResponseEntity<Location> tracking(@RequestBody LocationDTO dto) throws ParseException {

        Location location = new Location();
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss:SSS");
        date =  new SimpleDateFormat("dd-MM-YYYY hh:mm:ss:SSS").parse(df.format(date));
        Timestamp timestamp = new Timestamp(date.getTime());
//        DateFormat df = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss:SSS");
//        timestamp = df.format(timestamp);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        location.setTime(timestamp);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(dto.getVehicle_id());
        location.setVehicle(vehicle);


        Location locationSave = locationService.createLocation(location);

        if(locationSave == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(locationSave);
    }

    @GetMapping("/trackingLocation/{id}")
    public ResponseEntity<List<Location>> locationList(@PathVariable int id){
        List<Location> locations = new ArrayList<>();
        locations = locationService.getListLocationByVehicle(id);
        if(locations.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(locations);
    }

}
