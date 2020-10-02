package fmalc.api.controller;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fmalc.api.dto.*;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Driver;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.enums.ScheduleConsginmentEnum;
import fmalc.api.enums.SearchTypeForDriverEnum;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/api/v1.0/schedules")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    NotificationService notificationService;

    // Get the list of schedules of the driver
    @GetMapping(value = "driver")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<List<ScheduleResponseDTO>> findByConsignmentStatusAndUsername(@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username) {
        try {
            List<Schedule> schedules = scheduleService.findByConsignmentStatusAndUsername(status, username);


            if (schedules == null) {
                return ResponseEntity.noContent().build();
            }
            List<ScheduleResponseDTO> consignmentResponses = new ArrayList<>(new ScheduleResponseDTO().mapToListResponse(schedules));

            return ResponseEntity.ok().body(consignmentResponses);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "first-consignment/{idDriver}")
    public ResponseEntity<Integer> findFirstConsignment(@PathVariable("idDriver") Integer id) {

        Integer idConsignment = scheduleService.findConsignmentFirst(id);
        if (idConsignment != null && idConsignment>0) {
            return ResponseEntity.ok().body(idConsignment);
        }
//        DetailedScheduleDTO detailedScheduleDTO = new DetailedScheduleDTO(schedule);
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<DetailedScheduleDTO> findById(@PathVariable("id") Integer id) {

        Schedule schedule = scheduleService.findById(id);
        if (schedule == null || schedule.equals("")) {
            return ResponseEntity.noContent().build();
        }else{
            if (schedule.getConsignment().getStatus() != ConsignmentStatusEnum.CANCELED.getValue()){
                DetailedScheduleDTO detailedScheduleDTO = new DetailedScheduleDTO(schedule);
                return ResponseEntity.ok().body(detailedScheduleDTO);
            }else{
                return ResponseEntity.noContent().build();
            }
        }
    }


    @PostMapping
    public ResponseEntity<ScheduleToConfirmDTO> createSchedule(@RequestBody RequestObjectDTO requestObjectDTO) throws ParseException {
        ScheduleToConfirmDTO scheduleToConfirmDTO;
        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        if (requestObjectDTO.getSchedule().size() > 0) {
            scheduleToConfirmDTO = scheduleService.scheduleConfirm(requestObjectDTO, requestObjectDTO.getDriver_sub());
        } else {
            List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
            Consignment consignment = consignmentResponseDTO.mapToEntity(requestObjectDTO.getNewConsignment());
            ConsignmentRequestDTO consignmentRequestDTO = requestObjectDTO.getConsignmentRequest();
            scheduleToConfirmDTO = scheduleService.scheduleReturn(consignment, consignmentRequestDTO, scheduleForConsignmentDTOS, requestObjectDTO.getDriver_sub());
        }
        return ResponseEntity.ok().body(scheduleToConfirmDTO);
    }



//    private ScheduleToConfirmDTO scheduleReturn(Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO, List<ScheduleForConsignmentDTO> scheduleds, int driver_sub) throws ParseException {
//        List<Vehicle> vehicles =
//                vehicleService.findVehicleForSchedule(consignment, consignmentRequestDTO, ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue());
//        int sizeVehicle = 0;
//        for (int i = 0; i < consignmentRequestDTO.getVehicles().size(); i++) {
//            String quantity = consignmentRequestDTO.getVehicles().get(i).getQuantity();
//            sizeVehicle += Integer.parseInt(quantity);
//        }
//        List<Vehicle> vehiclesSave = new ArrayList<>();
//        List<Driver> driversSave = new ArrayList<>();
//        List<ScheduleToConfirmDTO> scheduleToConfirmDTOS = new ArrayList<>();
//        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
//        ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
//        Schedule schedule = new Schedule();
//        List<VehicleForDetailDTO> vehicleForDetailDTOS = new ArrayList<>();
//        VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
//        List<Driver> drivers = new ArrayList<>();
//        if (vehicles.size() > 0 && vehicles.size() >= sizeVehicle) {
//            Collections.sort(vehicles, new Comparator<Vehicle>() {
//                @Override
//                public int compare(Vehicle o1, Vehicle o2) {
//                    return o1.getKilometerRunning().compareTo(o2.getKilometerRunning());
//                }
//            });
//
//            Collections.sort(vehicles, new Comparator<Vehicle>() {
//                @Override
//                public int compare(Vehicle o1, Vehicle o2) {
//                    return o1.getWeight().compareTo(o2.getWeight());
//                }
//            });
//            List<VehicleConsignmentDTO> vehicleConsignmentDTOS = consignmentRequestDTO.getVehicles();
//            for(int  v =0; v<vehicleConsignmentDTOS.size(); v++){
//                List<Vehicle> vehiclesTmp = new ArrayList<>();
//                int size = Integer.parseInt(vehicleConsignmentDTOS.get(v).getQuantity());
//                for (int i = 0; i < vehicles.size(); i++) {
//                    if (scheduleds.size() > 0) {
//                        for (int s = 0; s < scheduleds.size(); s++) {
//                            if (vehicles.get(i).getId() != scheduleds.get(s).getVehicle().getId() && !vehiclesSave.contains(vehicles.get(i))) {
//                                if(vehicles.get(i).getWeight()>=  Double.parseDouble(vehicleConsignmentDTOS.get(v).getWeight())){
//                                    if(!vehiclesTmp.contains(vehicles.get(i))){
//                                        vehiclesTmp.add(vehicles.get(i));
//                                        if(size>0){
//                                            size--;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        if (!vehiclesSave.contains(vehicles.get(i))) {
//                            if(vehicles.get(i).getWeight()>=  Double.parseDouble(vehicleConsignmentDTOS.get(v).getWeight())){
//                                if(!vehiclesTmp.contains(vehicles.get(i))){
//                                    vehiclesTmp.add(vehicles.get(i));
//                                    if(size>0){
//                                        size--;
//                                    }
//                                }
//                            }
////                            vehiclesSave.add(vehicles.get(i));
//                        }
//                    }
//                    if(size == 0){
//                        vehiclesSave.addAll(vehiclesTmp);
//                        break;
//                    }
//                }
//            }
//
////            scheduleToConfirmDTO.setVehicles(vehicleForDetailDTO.mapToListResponse(vehiclesSave));
//            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTO.mapToListResponse(vehiclesSave));
//        } else if (vehiclesSave.size() > 0 && vehiclesSave.size() < sizeVehicle) {
//            vehicleForDetailDTOS = vehicleForDetailDTO.mapToListResponse(vehiclesSave);
//            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTOS);
//
//            // thieu xe
//        }
//
//        List<Driver> resultDriver = new ArrayList<>();
//        DriverForScheduleDTO driverForScheduleDTO = new DriverForScheduleDTO();
//        if (vehicles.size() > 0 && vehicles.size() >= sizeVehicle) {
//            for (int i = 0; i < vehicles.size(); i++) {
//
//
//                resultDriver = driverService.findDriverForSchedule(vehicles.get(i).getWeight(), consignment);
//                for (int j = 0; j < resultDriver.size(); j++) {
//                    if (scheduleds.size() > 0) {
//                        for (int s = 0; s < scheduleds.size(); s++) {
//                            if (resultDriver.get(j).getId() != scheduleds.get(s).getDriver().getId() && !drivers.contains(resultDriver.get(j))) {
//                                drivers.add(resultDriver.get(j));
//                            }
//                        }
//
//                    } else {
//                        if (!drivers.contains(resultDriver.get(j))) {
//                            drivers.add(resultDriver.get(j));
//                        }
//                    }
//
//                }
//
//
//            }
//        } else {
//        }
//        if (drivers.size() > 0 && drivers.size() >= sizeVehicle) {
//            Collections.sort(drivers, (o1, o2) -> o1.getWorkingHour().compareTo(o2.getWorkingHour()));
//            drivers.sort(new Comparator<Driver>() {
//                @Override
//                public int compare(Driver o1, Driver o2) {
//
//                    if(o1.getSchedules().size()<o2.getSchedules().size()){
//                        return -1;
//                    }else if(o1.getSchedules().size()>o2.getSchedules().size()){
//                        return 1;
//                    }else{
//                        return 0;
//                    }
//
//                }
//            });
//            for (int v = 0; v < vehiclesSave.size(); v++) {
//                for (int k = 0; k < drivers.size(); k++) {
//                    schedule = new Schedule();
//                    if (!driversSave.contains(drivers.get(k))) {
//
//                        int license = drivers.get(k).getDriverLicense();
//                        if (license == 0 && vehiclesSave.get(v).getWeight() < 3.5) {
//                            driversSave.add(drivers.get(k));
//                            schedule.setConsignment(consignment);
//                            schedule.setImageConsignment("no");
//                            schedule.setId(null);
//                            schedule.setDriver(drivers.get(k));
//                            schedule.setVehicle(vehiclesSave.get(v));
//                            schedule.setIsApprove(false);
////                            schedule = scheduleService.createSchedule(schedule);
//                            if (schedule != null) {
//
//                                scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
//                            }
//                            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
//                            k = drivers.size();
//                        } else if (license > 0) {
//                            driversSave.add(drivers.get(k));
//                            schedule.setConsignment(consignment);
//                            schedule.setImageConsignment("no");
//                            schedule.setId(null);
//                            schedule.setDriver(drivers.get(k));
//                            schedule.setVehicle(vehiclesSave.get(v));
//                            schedule.setIsApprove(false);
//                            if (schedule != null) {
//
//                                scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
//                            }
//                            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
//                            k = drivers.size();
//                        }
//
//                    }
//
//                }
//            }
//
//
//        } else if (drivers.size() > 0 && drivers.size() <= sizeVehicle) {
//            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
//        }
//        List<ScheduleForConsignmentDTO> list = new ArrayList<>();
//        if (scheduleForConsignmentDTOS.size() > 0 && driver_sub == 2) {
//            for (int i = 0; i < scheduleForConsignmentDTOS.size(); i++) {
//                ScheduleForConsignmentDTO scheduleForConsignmentDTO = new ScheduleForConsignmentDTO();
//                scheduleForConsignmentDTO = scheduleForConsignmentDTOS.get(i);
////            Schedule tmp = new Schedule();
//                for (int t = 0; t < drivers.size(); t++) {
//                    if (!driversSave.contains(drivers.get(t))) {
//                        int license1 = drivers.get(t).getDriverLicense();
//                        ScheduleForConsignmentDTO scheduleForConsignment = new ScheduleForConsignmentDTO();
////                        scheduleForConsignment = scheduleForConsignmentDTO;
//                        if (license1 > 0) {
//                            driversSave.add(drivers.get(t));
//                            driverForScheduleDTO = new DriverForScheduleDTO();
//                            driverForScheduleDTO = driverForScheduleDTO.convertToDto(drivers.get(t));
//                            scheduleForConsignmentDTO.setInheritance(driverForScheduleDTO);
//                            t = drivers.size();
//                            sizeVehicle--;
////                            list.add(scheduleForConsignment);
//                        } else if (license1 == 0 && scheduleForConsignmentDTO.getVehicle().getWeight() < 3.5) {
//                            driversSave.add(drivers.get(t));
//                            driverForScheduleDTO = new DriverForScheduleDTO();
//                            driverForScheduleDTO = driverForScheduleDTO.convertToDto(drivers.get(t));
//                            scheduleForConsignmentDTO.setInheritance(driverForScheduleDTO);
//                            t = drivers.size();
//                            sizeVehicle--;
////                            list.add(scheduleForConsignment);
//                        }
//                    }
//                }
//            }
//            scheduleForConsignmentDTOS.addAll(list);
//        }
//
//        scheduleToConfirmDTO.setScheduleForConsignmentDTOS(scheduleForConsignmentDTOS);
//        if (scheduleToConfirmDTO.getScheduleForConsignmentDTOS().size() == sizeVehicle) {
//            scheduleToConfirmDTO.setQuantity(0);
//        } else {
//            scheduleToConfirmDTO.setQuantity(sizeVehicle - scheduleToConfirmDTO.getScheduleForConsignmentDTOS().size());
//        }
////        scheduleToConfirmDTOS.add(scheduleToConfirmDTO);
//        return scheduleToConfirmDTO;
//    }

    @PostMapping("/status")
    public ResponseEntity<ConsignmentResponseDTO> createScheudles(@RequestPart(value = "file") MultipartFile file, @ModelAttribute(value = "requestSaveScheObjDTO") String requestSaveScheObjDTO) {

        try{
            ConsignmentResponseDTO consignmentResponseDTO = scheduleService.createSchedules(file,requestSaveScheObjDTO);
            return  ResponseEntity.ok().body(consignmentResponseDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<List<ScheduleForConsignmentDTO>> getScheduleForVehicle(@PathVariable int id) {
        List<ScheduleForConsignmentDTO> result = new ArrayList<>();
        try {
            result = scheduleService.getScheduleForVehicle(id);
            if (result.size() > 0) {
                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
//        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/id/{id}")
    public ResponseEntity<StatusToUpdateDTO> updateStatus(@PathVariable("id") Integer
                                                                  id, @RequestBody StatusToUpdateDTO statusToUpdateDTO) {
        Schedule schedule = scheduleService.findById(id);
        StatusToUpdateDTO status = new StatusToUpdateDTO();
        status = scheduleService.updateStautsForVeDriAndCon(statusToUpdateDTO, schedule);
        if (status != null) {
            return ResponseEntity.ok().body(status);
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping("/search")
//    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<List<ScheduleResponseDTO>> searchByTypeForDriver(@RequestParam SearchTypeForDriverEnum searchType,
                                                                           @RequestParam String searchValue, @RequestParam Integer driverId) {
        List<Schedule> result = new ArrayList<>();
        try {
            result = scheduleService.searchByTypeForDriver(searchValue, searchType,driverId);
            if (result.size() > 0) {
                List<ScheduleResponseDTO> consignmentResponses = new ArrayList<>(new ScheduleResponseDTO().mapToListResponse(result));
                return ResponseEntity.ok().body(consignmentResponses);
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
//        return ResponseEntity.ok().body(result);
    }

    @GetMapping("driver/{id}")
    public ResponseEntity<Integer> checkScheduleADriver(@PathVariable("id") int id) {
        int count = scheduleService.checkConsignmentStatus(id, ConsignmentStatusEnum.DELIVERING.getValue(), ConsignmentStatusEnum.OBTAINING.getValue());
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("driver/complete-consignment/{id}")
    public ResponseEntity<Integer> countCosignmentInDate(@PathVariable("id") int id) {

        Date date = new Date();
        Date endDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        endDate = cal.getTime();
        int count = scheduleService.countScheduleNumberInADayOfDriver(id, new Timestamp(date.getTime()), new Timestamp(endDate.getTime()));
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("driver/running/{id}")
    public ResponseEntity<DetailedScheduleDTO> getScheduleRunningForDriver(@PathVariable("id") int id) {
        Schedule schedule = new Schedule();
        try {
            schedule = scheduleService.getScheduleRunningForDriver(id);
            if (schedule != null) {

                DetailedScheduleDTO detailedScheduleDTO = new DetailedScheduleDTO(schedule);
                return ResponseEntity.ok().body(detailedScheduleDTO);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("id")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<Integer> findScheduleIdByConsignmentIdAndDriverId(@RequestParam("consignmentId") Integer consignmentId,
                                            @RequestParam("driverId") Integer driverId){
        try {
            Integer id = scheduleService.findScheduleIdByConsignmentIdAndDriverId(consignmentId, driverId);
            if (id == null){
                return ResponseEntity.noContent().build();
            }else{
                return ResponseEntity.ok().body(id);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/schedule-detail")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<Integer> findScheduleIdByContentOfNotificationAndDriverId(@RequestParam("content") String content,
                                            @RequestParam("driverId") Integer driverId){
        try {
            Integer id = scheduleService.findScheduleIdByContentOfNotificationAndDriverId(content, driverId);
            if (id == null){
                return ResponseEntity.noContent().build();
            }else{
                return ResponseEntity.ok().body(id);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
