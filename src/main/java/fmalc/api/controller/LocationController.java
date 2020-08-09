

package fmalc.api.controller;


import fmalc.api.dto.*;

import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.service.*;
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
    private int interval = 1000 * 60 *3; // 1 sec
    private int sizeHash = 0;
    private Flux<Long> intervals = Flux.interval(Duration.ofSeconds(5));
    private List<Integer> idVehicles = new ArrayList<>();
    List<Location> locations = new ArrayList<>();

    @PostMapping("/sendLocation")
    public ResponseEntity<String> tracking(@RequestBody LocationDTO dto) throws ParseException {
        System.out.println("Send");
        Location location = new Location();
//        location.setId(null);
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
        System.out.println(tracking.size());

        tracking.put(location, location.getSchedule().getId());
        if (locations.size() <= 0) {
            locations.add(location);
        } else if (!locations.contains(location.getAddress())) {
            locations.add(location);
        }
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
        return ResponseEntity.ok().body("OK");

    }

    @GetMapping("/stop-tracking/{id}")
    public ResponseEntity<String> stopTracking(@PathVariable("id") int id){
//        List<ScheduleForConsignmentDTO>  list =vehicleService.checkScheduleForVehicle(id);
        List<Schedule> schedules = scheduleService.checkVehicleInScheduled(id);
        List<Schedule> result = new ArrayList<>();
        for(int i=0 ; i<schedules.size(); i++){
            if(schedules.get(i).getConsignment().getStatus()== ConsignmentStatusEnum.DELIVERING.getValue()
            || schedules.get(i).getConsignment().getStatus()== ConsignmentStatusEnum.OBTAINING.getValue()){
                result.add(schedules.get(i));
            }
        }
        if(result.size()==0){
            for (Map.Entry key : tracking.entrySet()) {
                if(schedules.contains(key.getValue())){
                    tracking.remove(key);
                }
            }

        }else{
            for (Map.Entry key : tracking.entrySet()) {
                if(schedules.contains(key.getValue()) && !result.contains(key.getValue())){
                    tracking.remove(key);
                }
            }
        }
        return  ResponseEntity.ok().body("OK");
    }

//    @PostMapping()
//    public ResponseEntity<String> save(@RequestBody LocationDTO dto) throws ParseException {
//        System.out.println("Send");
//        Location location = new Location();
////        location.setId(null);
//        location.setLatitude(dto.getLatitude());
//        location.setLongitude(dto.getLongitude());
//        location.setAddress(dto.getAddress());
//        Schedule schedule = scheduleService.findById(dto.getSchedule());
//
//        Date date = new Date();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        df.getTimeZone();
//        date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(df.format(date));
//        Timestamp timestamp = new Timestamp(date.getTime());
//        location.setTime(timestamp);
//        location.setSchedule(schedule);
////        if(schedule.get)
//        locationService.createLocation(location);
//        return ResponseEntity.ok().body("OK");
//
//    }

    @GetMapping("consignment/id/{id}")
    public ResponseEntity<List<LocationResponeDTO>> getLocationAConsignment(@PathVariable("id") int idConsignment){
        try{
            List<LocationResponeDTO> locationResponeDTOS = new ArrayList<>();
            List<Location> locations = new ArrayList<>();
            List<Schedule> schedules = consignmentService.findScheduleByConsignment(idConsignment);
            if(schedules.size()>0){
               for(int i =0; i< schedules.size(); i++){
                   List<Location> tmp = locationService.getListLocationBySchedule(schedules.get(i).getId());
                   if(tmp.size()>0){
                       locations.addAll(tmp);
                   }

               }
            }


            if(locations.size()>0){
                locationResponeDTOS = new LocationResponeDTO().mapToListResponse(locations);
                return  ResponseEntity.ok().body(locationResponeDTOS);
            }
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
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
    public Flux<List<LocationResponeDTO>> locationListForVehicle(@PathVariable("id") int id) {
        idVehicles.add(id);
        intervals.subscribe((i) -> SSELocationForVehicle(id));
        if (SSELocationForVehicle(id).size() <= 0) {
            Disposable disposable = intervals.subscribe();
            disposable.dispose();
        }
        Flux<List<LocationResponeDTO>> transactionFlux = Flux.fromStream(Stream.generate(() -> SSELocationForVehicle(id)));
        Flux<List<LocationResponeDTO>> flux = Flux.zip(intervals, transactionFlux).map(Tuple2::getT2);
        return flux;
    }

    private List<LocationResponeDTO> SSELocationForVehicle(int id) {
        System.out.println(tracking.size());
        int size = tracking.size();
        ScheduleDTO scheduleForLocationDTO = new ScheduleDTO();
        List<Location> locationLists = new ArrayList();
        LocationResponeDTO locationResponeDTO = new LocationResponeDTO();
        List<LocationResponeDTO> locationDTOS = new ArrayList();
        if (idVehicles.size() <= 0) {
            locationDTOS = new ArrayList();
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
        locationDTOS.sort(Comparator.comparing(LocationResponeDTO::getTime));
//        Collections.sort(locationDTOS, new Comparator<LocationResponeDTO>() {
//            @Override
//            public int compare(LocationResponeDTO o1, LocationResponeDTO o2) {
//                return o2.getTime().compareTo(o1.getTime());
//            }
//        });
        return locationDTOS;
    }

    @GetMapping("/disconnect/{id}")
    public ResponseEntity disconnect(@PathVariable("id") int id) {
        if (idVehicles.contains(id) && idVehicles.size() > 0) {
            System.out.println(idVehicles.size() + "size");
            if (idVehicles.size() == 1) {
                System.out.println("idVE" + idVehicles.size());
            } else {

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
                    Schedule schedule =scheduleService.findById(id);
                    if (schedule.getConsignment().getStatus() == ConsignmentStatusEnum.OBTAINING.getValue()
                    ||schedule.getConsignment().getStatus() == ConsignmentStatusEnum.DELIVERING.getValue()) {
                        Location locationSave = (Location) key.getKey();
                        locationLists.add(locationSave);
                    } else if(schedule.getConsignment().getStatus() == ConsignmentStatusEnum.COMPLETED.getValue()) {
                        List<Location> locationList = locationService.getListLocationBySchedule(id);
                        if(locationList.size()>0){
                            locationDTOS = new LocationResponeDTO().mapToListResponse(locationList);
                        }
                    }
                } else {
                    return locationDTOS;
                }
            }
            locationDTOS = new LocationResponeDTO().mapToListResponse(locationLists);

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

