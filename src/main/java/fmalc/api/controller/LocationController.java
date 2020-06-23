package fmalc.api.controller;


import fmalc.api.dto.LocationDTO;

import fmalc.api.dto.LocationResponeDTO;
import fmalc.api.dto.VehicleForDetailDTO;
import fmalc.api.entity.Alert;
import fmalc.api.entity.Location;
import fmalc.api.entity.Vehicle;
import fmalc.api.service.LocationService;
import fmalc.api.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private VehicleService vehicleService;

    private HashMap<Location, Integer> tracking = new HashMap<>();
    private int interval = 1000 * 60; // 1 sec

    private int siez = 0;

    @PostMapping("/sendLocation")
    public ResponseEntity<HashMap<Object, Object>> tracking(@RequestBody LocationDTO dto) throws ParseException {
        Location location = new Location();
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setAddress(dto.getAddress());

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(df.format(date));
        Timestamp timestamp = new Timestamp(date.getTime());
        location.setTime(timestamp);
        Vehicle vehicle;
        vehicle = vehicleService.findVehicleByIdForLocation(dto.getVehicle_id());
//        vehicle.setId(dto.getVehicle_id());
        location.setVehicle(vehicle);
        HashMap<Object, Object> locationHashMap = new HashMap<>();
        tracking.put(location, location.getVehicle().getId());
        for (Map.Entry key : tracking.entrySet()) {

            if (key.getValue() == location.getVehicle().getId()) {
                locationHashMap.put(key.getKey(), key.getValue());
            }
        }
        int sizetmp = tracking.size();
        if (sizetmp == siez) {

        } else {
            Date timeToRun = new Date(System.currentTimeMillis() + interval);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    for (Map.Entry key : tracking.entrySet()) {
                        Location locationSave = (Location) key.getKey();
                        if (locationSave != null) {
                            locationService.createLocation(locationSave);
                        }

                    }
                }
            }, timeToRun);
            sizetmp = siez;
        }

        return ResponseEntity.ok().body(locationHashMap);


    }

    @GetMapping(value = "/trackingLocation/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<LocationResponeDTO>> locationList(@PathVariable int id) {


        Flux<Long> intervals = Flux.interval(Duration.ofSeconds(5));
        intervals.subscribe((i) -> SSELocation(id));
        Flux<List<LocationResponeDTO>> transactionFlux = Flux.fromStream(Stream.generate(() -> SSELocation(id)));
//        List<Location> locationList =  Flux.fromIterable(SSELocation(id)).filter(location->location.getVehicle_id() == id);
        if (SSELocation(id).size() <= 0) {
            Disposable disposable = intervals.subscribe();
            disposable.dispose();
        }
        System.out.println("ssssssssssss");
        return Flux.zip(intervals, transactionFlux).map(Tuple2::getT2);

    }


//    @GetMapping(value = "/movement/{id}", produces= MediaType.TEXT_EVENT_STREAM_VALUE )
//    public ResponseEntity<Alert> notifyMoving(@PathVariable int id){
//
//
//    }

    private List<LocationResponeDTO> SSELocation(int id) {
        int size = tracking.size();
        List<Location> locationLists = new ArrayList();
        List<LocationResponeDTO> locationDTOS = new ArrayList();
        if (size > 0) {
            for (Map.Entry key : tracking.entrySet()) {
                if ((Integer) key.getValue() == id) {
                    if (vehicleService.findVehicleByIdForLocation(id).getStatus() == 2) {
                        Location locationSave = (Location) key.getKey();
//                    System.out.println("CCCCC");
                        locationLists.add(locationSave);
                    } else {
                        tracking.remove(key.getKey());
                    }

                } else {
                    return locationDTOS;
                }
            }
            locationDTOS = locationLists.stream().map(this::convertToDto).collect(Collectors.toList());
//            sizeMap = tracking.size();

        } else {

        }
        return locationDTOS;
    }

    private LocationResponeDTO convertToDto(Location location) {
        ModelMapper modelMapper = new ModelMapper();
        LocationResponeDTO dto = modelMapper.map(location, LocationResponeDTO.class);

        return dto;
    }

}
