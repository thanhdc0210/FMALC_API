

package fmalc.api.controller;


import fmalc.api.dto.*;

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
    private Flux<Long> intervals = Flux.interval(Duration.ofSeconds(5));
    private List<Integer> idVehicles = new ArrayList<>();
    List<Location> locations = new ArrayList<>();

    @PostMapping("/sendLocation")
    public ResponseEntity<String> tracking(@RequestBody LocationDTO dto) throws ParseException {
        System.out.println("Send");
        Location location = new Location();
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setAddress(dto.getAddress());
        Schedule schedule = scheduleService.findById(dto.getSchedule());
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        df.getTimeZone();
        date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(df.format(date));
        Timestamp timestamp = new Timestamp(date.getTime());

        location.setTime(timestamp);

        location.setSchedule(schedule);

        tracking.put(location, location.getSchedule().getId());
        if (locations.size() <= 0) {

            locations.add(location);
        } else if (!locations.contains(location.getAddress())) {
            locations.add(location);
        }


//        int sizetmp = locations.size();
//        if (sizetmp == sizeHash) {
//        } else {
            Date timeToRun = new Date(System.currentTimeMillis() + interval);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {

                    if (locations.size() > 0) {
                        Location locationTmp = locations.get(0);
                        locationService.createLocation(locationTmp);
                        for (int i = 0; i < locations.size(); i++) {
                            if (locationTmp.getAddress() != locations.get(i).getAddress() ||
                                    locationTmp.getSchedule().getId() != locations.get(i).getSchedule().getId()) {
                                locationService.createLocation(locations.get(i));
                            }


                        }
                    }
                    locations = new ArrayList<>();
                }
            }, timeToRun);
//            sizeHash = sizetmp;
//        }
        return ResponseEntity.ok().body("ok");
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
        idVehicles.add(id);
        intervals.subscribe((i) -> SSELocationForVehicle(id));
        if (SSELocationForVehicle(id).size() <= 0) {
            Disposable disposable = intervals.subscribe();
            disposable.dispose();
        }
        Flux<List<LocationResponeDTO>> transactionFlux = Flux.fromStream(Stream.generate(() -> SSELocationForVehicle(id)));
        Flux<List<LocationResponeDTO>> flux = Flux.zip(intervals, transactionFlux).map(Tuple2::getT2);
//        if(idVehicles)

        return flux;
    }

    private List<LocationResponeDTO> SSELocationForVehicle(int id) {
        int size = tracking.size();
        ScheduleDTO scheduleForLocationDTO = new ScheduleDTO();
        List<Location> locationLists = new ArrayList();
        LocationResponeDTO locationResponeDTO = new LocationResponeDTO();
        List<LocationResponeDTO> locationDTOS = new ArrayList();
        if (idVehicles.size() <= 0) {
            locationDTOS = new ArrayList();
//            intervals.subscribe(Disposable () -> );
            Disposable disposable = intervals.subscribe();
            disposable.dispose();
        } else {
            if (size > 0) {
                for (Map.Entry key : tracking.entrySet()) {
                    Schedule schedules = scheduleService.findById((Integer) key.getValue());
//                    scheduleForLocationDTO = scheduleForLocationDTO.convertSchedule(schedules);
                    VehicleForDetailDTO vehicleForDetailDTO = vehicleService.findVehicleById(schedules.getVehicle().getId());
                    if (schedules != null) {
                        if (idVehicles.contains(schedules.getVehicle().getId())) {
                            if (vehicleForDetailDTO.getId() == id) {
                                if (vehicleForDetailDTO.getStatus() == 2) {
                                    Location locationSave = (Location) key.getKey();
                                    locationSave.setSchedule(schedules);
                                    locationLists.add(locationSave);
                                } else {
                                    tracking.remove(key.getKey());
                                }
                            } else {
                            }
                        } else {
                        }
                    }

                }
                locationDTOS = locationResponeDTO.mapToListResponse(locationLists);
//                locationDTOS = locationLists.stream().map(this::convertToDto).collect(Collectors.toList());

            } else {

            }
        }

        return locationDTOS;
    }

    @GetMapping("/disconnect/{id}")
    public ResponseEntity disconnect(@PathVariable("id") int id) {
//        System.out.println(id);
        if (idVehicles.contains(id) && idVehicles.size() > 0) {
            System.out.println(idVehicles.size() + "size");
//            idVehicles.remove(id);
            if (idVehicles.size() == 1) {
//                idVehicles = new ArrayList<>();
                System.out.println("idVE" + idVehicles.size());
            } else {
//                idVehicles.remove(id);
            }

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }

    private List<LocationResponeDTO> SSELocation(int id) {
        int size = tracking.size();
        List<Location> locationLists = new ArrayList();
        List<LocationResponeDTO> locationDTOS = new ArrayList();
        if (size > 0) {
            for (Map.Entry key : tracking.entrySet()) {
                if ((Integer) key.getValue() == id) {
                    if (scheduleService.findById(id).getVehicle().getStatus() == 2) {
                        Location locationSave = (Location) key.getKey();
                        locationLists.add(locationSave);
                    } else {
                        tracking.remove(key.getKey());
                    }
                } else {
                    return locationDTOS;
                }
            }
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

