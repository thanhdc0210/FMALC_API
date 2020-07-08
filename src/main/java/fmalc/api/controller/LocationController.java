

package fmalc.api.controller;


import fmalc.api.dto.LocationDTO;

import fmalc.api.dto.LocationResponeDTO;
import fmalc.api.dto.ScheduleForLocationDTO;
import fmalc.api.dto.VehicleForDetailDTO;
import fmalc.api.entity.*;
import fmalc.api.service.ConsignmentService;
import fmalc.api.service.LocationService;
import fmalc.api.service.ScheduleService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1.0/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private ConsignmentService consignmentService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ScheduleService scheduleService;

    private HashMap<Location, Integer> tracking = new HashMap<>();
    private int interval = 1000 * 60; // 1 sec
    private int sizeHash = 0;
    private  Flux<Long> intervals = Flux.interval(Duration.ofSeconds(5));
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
        Consignment consignment;
        consignment = consignmentService.findById(dto.getConsignment());
//        vehicle.setId(dto.getVehicle_id());
        location.setConsignment(consignment);
        HashMap<Object, Object> locationHashMap = new HashMap<>();
        tracking.put(location, location.getConsignment().getId());
        for (Map.Entry key : tracking.entrySet()) {

            if (key.getValue() == location.getConsignment().getId()) {
                locationHashMap.put(key.getKey(), key.getValue());
            }
        }
        int sizetmp = tracking.size();
        if (sizetmp == sizeHash) {
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
            sizeHash = sizetmp;
        }
        return ResponseEntity.ok().body(locationHashMap);
    }

    @GetMapping(value = "/trackingLocation/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<LocationResponeDTO>> locationListForConsignment(@PathVariable int id) {



        intervals.subscribe((i) -> SSELocation(id));
        Flux<List<LocationResponeDTO>> transactionFlux = Flux.fromStream(Stream.generate(() -> SSELocation(id)));
        if (SSELocation(id).size() <= 0) {
            Disposable disposable = intervals.subscribe();
            disposable.dispose();
        }

        return Flux.zip(intervals, transactionFlux).map(Tuple2::getT2);
    }

    @GetMapping(value = "/locationofveicle/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<LocationResponeDTO>> locationListForVehicle(@PathVariable int id) {


        Flux<Long> intervals = Flux.interval(Duration.ofSeconds(5));
        intervals.subscribe((i) -> SSELocationForVehicle(id));
        Flux<List<LocationResponeDTO>> transactionFlux = Flux.fromStream(Stream.generate(() -> SSELocationForVehicle(id)));
        if (SSELocationForVehicle(id).size() <= 0) {
            Disposable disposable = intervals.subscribe();
            disposable.dispose();
        }

        return Flux.zip(intervals, transactionFlux).map(Tuple2::getT2);
    }

    private List<LocationResponeDTO> SSELocationForVehicle(int id) {
        int size = tracking.size();
        List<Location> locationLists = new ArrayList();
        List<LocationResponeDTO> locationDTOS = new ArrayList();
        if (size > 0) {
            for (Map.Entry key : tracking.entrySet()) {
                List<ScheduleForLocationDTO> schedules = scheduleService.getScheduleByConsignmentId((Integer)key.getValue());
                for(int i =0; i<schedules.size(); i++){
                    VehicleForDetailDTO vehicle = vehicleService.findVehicleById(schedules.get(i).getVehicle_id());
                    if (vehicle.getId() == id) {
                        if (vehicle.getStatus() ==2) {
                            Location locationSave = (Location) key.getKey();
                            locationLists.add(locationSave);
                        } else {
                            tracking.remove(key.getKey());
                        }
                    } else {
                        //
                    }
                }
            }
            locationDTOS = locationLists.stream().map(this::convertToDto).collect(Collectors.toList());

        } else {

        }
        System.out.println("AAAAAAAAAAA");
        return locationDTOS;
    }

    private List<LocationResponeDTO> SSELocation(int id) {
        int size = tracking.size();
        List<Location> locationLists = new ArrayList();
        List<LocationResponeDTO> locationDTOS = new ArrayList();
        if (size > 0) {
//            for (Map.Entry key : tracking.entrySet()) {
//                if ((Integer) key.getValue() == id) {
//                    if (consignmentService.findById(id).getSchedule().getVehicle().getStatus() ==2) {
//                        Location locationSave = (Location) key.getKey();
//                        locationLists.add(locationSave);
//                    } else {
//                        tracking.remove(key.getKey());
//                    }
//                } else {
//                    return locationDTOS;
//                }
//            }
            locationDTOS = locationLists.stream().map(this::convertToDto).collect(Collectors.toList());

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

//=======
////
////package fmalc.api.controller;
////
////
////import fmalc.api.dto.LocationDTO;
////
////import fmalc.api.dto.LocationResponeDTO;
////import fmalc.api.dto.ScheduleForLocationDTO;
////import fmalc.api.dto.VehicleForDetailDTO;
////import fmalc.api.entity.*;
////import fmalc.api.service.ConsignmentService;
////import fmalc.api.service.LocationService;
////import fmalc.api.service.ScheduleService;
////import fmalc.api.service.VehicleService;
////import org.modelmapper.ModelMapper;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.MediaType;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////import reactor.core.Disposable;
////import reactor.core.publisher.Flux;
////import reactor.util.function.Tuple2;
////
////
////import java.sql.Timestamp;
////import java.text.DateFormat;
////import java.text.ParseException;
////import java.text.SimpleDateFormat;
////import java.time.Duration;
////import java.util.*;
////import java.util.List;
////import java.util.stream.Collectors;
////import java.util.stream.Stream;
////
////@RestController
////@RequestMapping("/api/v1.0/location")
////public class LocationController {
////
////    @Autowired
////    private LocationService locationService;
////
////    @Autowired
////    private ConsignmentService consignmentService;
////
////    @Autowired
////    private VehicleService vehicleService;
////
////    @Autowired
////    private ScheduleService scheduleService;
////
////    private HashMap<Location, Integer> tracking = new HashMap<>();
////    private int interval = 1000 * 60; // 1 sec
////    private int sizeHash = 0;
////    private  Flux<Long> intervals = Flux.interval(Duration.ofSeconds(5));
////    @PostMapping("/sendLocation")
////    public ResponseEntity<HashMap<Object, Object>> tracking(@RequestBody LocationDTO dto) throws ParseException {
////        Location location = new Location();
////        location.setLatitude(dto.getLatitude());
////        location.setLongitude(dto.getLongitude());
////        location.setAddress(dto.getAddress());
////
////        Date date = new Date();
////        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
////        date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(df.format(date));
////        Timestamp timestamp = new Timestamp(date.getTime());
////
////        location.setTime(timestamp);
////        Consignment consignment;
////        consignment = consignmentService.findById(dto.getConsignment());
//////        vehicle.setId(dto.getVehicle_id());
////        location.setConsignment(consignment);
////        HashMap<Object, Object> locationHashMap = new HashMap<>();
////        tracking.put(location, location.getConsignment().getId());
////        for (Map.Entry key : tracking.entrySet()) {
////
////            if (key.getValue() == location.getConsignment().getId()) {
////                locationHashMap.put(key.getKey(), key.getValue());
////            }
////        }
////        int sizetmp = tracking.size();
////        if (sizetmp == sizeHash) {
////        } else {
////            Date timeToRun = new Date(System.currentTimeMillis() + interval);
////            Timer timer = new Timer();
////            timer.schedule(new TimerTask() {
////                public void run() {
////                    for (Map.Entry key : tracking.entrySet()) {
////                        Location locationSave = (Location) key.getKey();
////                        if (locationSave != null) {
////                            locationService.createLocation(locationSave);
////                        }
////                    }
////                }
////            }, timeToRun);
////            sizeHash = sizetmp;
////        }
////        return ResponseEntity.ok().body(locationHashMap);
////    }
////
////    @GetMapping(value = "/trackingLocation/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
////    public Flux<List<LocationResponeDTO>> locationListForConsignment(@PathVariable int id) {
////
////
////
////        intervals.subscribe((i) -> SSELocation(id));
////        Flux<List<LocationResponeDTO>> transactionFlux = Flux.fromStream(Stream.generate(() -> SSELocation(id)));
////        if (SSELocation(id).size() <= 0) {
////            Disposable disposable = intervals.subscribe();
////            disposable.dispose();
////        }
////
////        return Flux.zip(intervals, transactionFlux).map(Tuple2::getT2);
////    }
////
////    @GetMapping(value = "/locationofveicle/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
////    public Flux<List<LocationResponeDTO>> locationListForVehicle(@PathVariable int id) {
////
////
////        Flux<Long> intervals = Flux.interval(Duration.ofSeconds(5));
////        intervals.subscribe((i) -> SSELocationForVehicle(id));
////        Flux<List<LocationResponeDTO>> transactionFlux = Flux.fromStream(Stream.generate(() -> SSELocationForVehicle(id)));
////        if (SSELocationForVehicle(id).size() <= 0) {
////            Disposable disposable = intervals.subscribe();
////            disposable.dispose();
////        }
////
////        return Flux.zip(intervals, transactionFlux).map(Tuple2::getT2);
////    }
////
////    private List<LocationResponeDTO> SSELocationForVehicle(int id) {
////        int size = tracking.size();
////        List<Location> locationLists = new ArrayList();
////        List<LocationResponeDTO> locationDTOS = new ArrayList();
////        if (size > 0) {
////            for (Map.Entry key : tracking.entrySet()) {
////                List<ScheduleForLocationDTO> schedules = scheduleService.getScheduleByConsignmentId((Integer)key.getValue());
////                for(int i =0; i<schedules.size(); i++){
////                    VehicleForDetailDTO vehicle = vehicleService.findVehicleById(schedules.get(i).getVehicle_id());
////                    if (vehicle.getId() == id) {
////                        if (vehicle.getStatus() ==2) {
////                            Location locationSave = (Location) key.getKey();
////                            locationLists.add(locationSave);
////                        } else {
////                            tracking.remove(key.getKey());
////                        }
////                    } else {
////                        //
////                    }
////                }
////            }
////            locationDTOS = locationLists.stream().map(this::convertToDto).collect(Collectors.toList());
////
////        } else {
////        }
////        System.out.println("AAAAAAAAAAA");
////        return locationDTOS;
////    }
////
////    private List<LocationResponeDTO> SSELocation(int id) {
////        int size = tracking.size();
////        List<Location> locationLists = new ArrayList();
////        List<LocationResponeDTO> locationDTOS = new ArrayList();
////        if (size > 0) {
////            for (Map.Entry key : tracking.entrySet()) {
////                if ((Integer) key.getValue() == id) {
////                    if (consignmentService.findById(id).getSchedule().getVehicle().getStatus() ==2) {
////                        Location locationSave = (Location) key.getKey();
////                        locationLists.add(locationSave);
////                    } else {
////                        tracking.remove(key.getKey());
////                    }
////                } else {
////                    return locationDTOS;
////                }
////            }
////            locationDTOS = locationLists.stream().map(this::convertToDto).collect(Collectors.toList());
////
////        } else {
////        }
////        return locationDTOS;
////    }
////
////    private LocationResponeDTO convertToDto(Location location) {
////        ModelMapper modelMapper = new ModelMapper();
////        LocationResponeDTO dto = modelMapper.map(location, LocationResponeDTO.class);
////
////        return dto;
////    }
////
////}
////
//>>>>>>> 4efc5f4dfe067aa2ffcc1771ff9887de0b016d9d
