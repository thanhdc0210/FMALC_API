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
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<DetailedScheduleDTO> findById(@PathVariable("id") Integer id) {

        Schedule schedule = scheduleService.findById(id);
        if (schedule == null || schedule.equals("")) {
            return ResponseEntity.noContent().build();
        }
        DetailedScheduleDTO detailedScheduleDTO = new DetailedScheduleDTO(schedule);
        return ResponseEntity.ok().body(detailedScheduleDTO);
    }


    @PostMapping
    public ResponseEntity<ScheduleToConfirmDTO> createSchedule(@RequestBody RequestObjectDTO requestObjectDTO) throws ParseException {
        ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        if (requestObjectDTO.getSchedule().size() > 0) {
            scheduleToConfirmDTO = schedulesConfirm(requestObjectDTO, requestObjectDTO.getDriver_sub());
        } else {
            List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
            Consignment consignment = consignmentResponseDTO.mapToEntity(requestObjectDTO.getNewConsignment());
            ConsignmentRequestDTO consignmentRequestDTO = requestObjectDTO.getConsignmentRequest();
            scheduleToConfirmDTO = scheduleReturn(consignment, consignmentRequestDTO, scheduleForConsignmentDTOS, requestObjectDTO.getDriver_sub());
        }


//        scheduleForConsignment
        return ResponseEntity.ok().body(scheduleToConfirmDTO);
    }

    private ScheduleToConfirmDTO schedulesConfirm(RequestObjectDTO requestObjectDTO, int driver_sub) throws ParseException {
        List<ScheduleToConfirmDTO> scheduleToConfirmDTOS = new ArrayList<>();
        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
        List<ObejctScheDTO> obejctScheDTOS = new ArrayList<>();
        Schedule schedule = new Schedule();
        List<Schedule> schedules = new ArrayList<>();
        DriverForScheduleDTO driverForScheduleDTO = new DriverForScheduleDTO();
        obejctScheDTOS = requestObjectDTO.getSchedule();
        ConsignmentRequestDTO consignmentRequestDTO = requestObjectDTO.getConsignmentRequest();
        ConsignmentRequestDTO consignmentRe = consignmentRequestDTO;
        VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
        List<VehicleConsignmentDTO> vehicleConsignmentDTOS = consignmentRequestDTO.getVehicles();
        vehicleConsignmentDTOS.sort((VehicleConsignmentDTO v1, VehicleConsignmentDTO v2) -> (v1.getWeight().compareTo(v2.getWeight())));
        Consignment consignment = consignmentResponseDTO.mapToEntity(requestObjectDTO.getNewConsignment());
        List<ObejctScheDTO> removeObject = new ArrayList<>();
        int total = 0;
        for (int obj = 0; obj < vehicleConsignmentDTOS.size(); obj++) {
            total += Integer.parseInt(vehicleConsignmentDTOS.get(obj).getQuantity());
        }
        for (int i = 0; i < vehicleConsignmentDTOS.size(); i++) {
            double weight = Double.parseDouble(vehicleConsignmentDTOS.get(i).getWeight());
            int quan = Integer.parseInt(vehicleConsignmentDTOS.get(i).getQuantity());
            if (removeObject.size() > 0) {
                removeObject = new ArrayList<>();
            }
            for (int j = 0; j < obejctScheDTOS.size(); j++) {
//                int count = 1;
                schedule = scheduleService.findScheduleByVehDriCon(obejctScheDTOS.get(j));
                if (schedule.getVehicle().getWeight() == weight && quan > 0) {
                    schedules.add(schedule);
                    quan = quan - 1;
                    removeObject.add(obejctScheDTOS.get(j));
                }
                if (quan == 0 && removeObject.size() < obejctScheDTOS.size()) {
                    j = obejctScheDTOS.size();
                } else if (quan == 0 && obejctScheDTOS.size() == removeObject.size()) {
                    j = obejctScheDTOS.size();
                } else if (quan > 0 && obejctScheDTOS.size() == removeObject.size()) {
                    j = obejctScheDTOS.size();
                }
            }
            vehicleConsignmentDTOS.get(i).setQuantity(String.valueOf(quan));

            if (quan > 0) {
                for (int j = 0; j < obejctScheDTOS.size(); j++) {
//                int count = 1;
                    schedule = scheduleService.findScheduleByVehDriCon(obejctScheDTOS.get(j));
                    if (schedule.getVehicle().getWeight() > weight && quan > 0) {
                        schedules.add(schedule);
                        quan = quan - 1;
                        removeObject.add(obejctScheDTOS.get(j));
                    }
                    if (quan == 0 && removeObject.size() < obejctScheDTOS.size()) {
                        j = obejctScheDTOS.size();
                    } else if (quan == 0 && obejctScheDTOS.size() == removeObject.size()) {
                        j = obejctScheDTOS.size();
                    } else if (quan > 0 && obejctScheDTOS.size() == removeObject.size()) {
                        j = obejctScheDTOS.size();
                    }
                }
                vehicleConsignmentDTOS.get(i).setQuantity(String.valueOf(quan));
            }

            if (schedules.size() == obejctScheDTOS.size()) {
//                if (i < vehicleConsignmentDTOS.size() - 1) {
//                    consignmentRequestDTO.setVehicles(vehicleConsignmentDTOS);
////                    List<Vehicle> vehicles =
////                            scheduleService.findVehicleForSchedule(consignment, consignmentRequestDTO);
//                    scheduleToConfirmDTOS = scheduleReturn( consignment, consignmentRequestDTO);
                i = vehicleConsignmentDTOS.size();
//                }
            }
            obejctScheDTOS.removeAll(removeObject);
//            if(vehicleConsignmentDTOS.get(i).getWeight() >= )
        }

        List<Vehicle> vehicles = new ArrayList<>();
        List<Driver> drivers = new ArrayList<>();
        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
        consignmentRequestDTO.setVehicles(vehicleConsignmentDTOS);
        vehicles = vehicleService.findVehicleForSchedule(consignment, consignmentRequestDTO, ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue());
        for (int ve = 0; ve < vehicles.size(); ve++) {
            List<Driver> driverList = new ArrayList<>();
            driverList = driverService.findDriverForSchedule(vehicles.get(ve).getWeight(), consignment);
            for (int dri = 0; dri < driverList.size(); dri++) {
                if (!drivers.contains(driverList.get(dri))) {
                    drivers.add(driverList.get(dri));
                }
            }
//                                driverList.removeIf(driver -> {drivers.contains(driver)});
        }
        if (total <= schedules.size()) {
            for (int sch = 0; sch < total; sch++) {
//                List<Driver> resultDriver = driverService.findDriverForSchedule(schedules.get(sch).getVehicle().getWeight(), consignment);
                schedule = new Schedule();
                if (schedules.get(sch).getInheritance() == null) {
                    schedule.setVehicle(schedules.get(sch).getVehicle());
                    schedule.setDriver(schedules.get(sch).getDriver());
                    schedule.setIsApprove(false);
                    schedule.setConsignment(consignment);
                    schedule.setImageConsignment(consignmentRequestDTO.getImageConsignment());
//                    schedule =  scheduleService.createSchedule(schedule);
                    if (schedule != null) {
                        if (driver_sub == 2) {
                            Schedule tmp = new Schedule();
                            tmp = scheduleService.getScheduleByDriverSub(schedules.get(sch).getId());
                            if (tmp != null) {
                                scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(tmp));
                            } else {
                                tmp = schedules.get(sch);
                                Driver driver = new Driver();
                                if (drivers.size() > 0) {
                                    for (int i = 0; i < drivers.size(); i++) {
                                        driver = drivers.get(i);
                                        if (driver != schedule.getDriver()) {
                                            tmp.setDriver(driver);
                                        }
                                    }
                                }
                                Vehicle vehicle = new Vehicle();
                                if (vehicles.size() > 0) {
                                    for (int i = 0; i < vehicles.size(); i++) {
                                        vehicle = vehicles.get(i);
                                        if (vehicle != schedule.getVehicle()) {
                                            tmp.setVehicle(vehicle);
                                        }
                                    }
                                }
                                scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(tmp));
                            }
                        }

                        scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
                    }
                }

            }

            scheduleToConfirmDTO.setScheduleForConsignmentDTOS(scheduleForConsignmentDTOS);
            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTO.mapToListResponse(vehicles));

        } else if (total >= schedules.size()) {
            for (int sch = 0; sch < schedules.size(); sch++) {
                schedule = new Schedule();
                schedule.setVehicle(schedules.get(sch).getVehicle());
                schedule.setDriver(schedules.get(sch).getDriver());
                schedule.setIsApprove(false);
                schedule.setConsignment(consignment);
                schedule.setImageConsignment(consignmentRequestDTO.getImageConsignment());

                if (schedule != null) {
                    scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
                }
            }
            vehicles = vehicleService.findVehicleForSchedule(consignment, consignmentRequestDTO, ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue());
            for (int ve = 0; ve < vehicles.size(); ve++) {
                List<Driver> driverList = new ArrayList<>();
                driverList = driverService.findDriverForSchedule(vehicles.get(ve).getWeight(), consignment);
                for (int dri = 0; dri < driverList.size(); dri++) {
                    if (!drivers.contains(driverList.get(dri))) {
                        drivers.add(driverList.get(dri));
                    }
                }
//                                driverList.removeIf(driver -> {drivers.contains(driver)});
            }


            scheduleToConfirmDTO.getScheduleForConsignmentDTOS().addAll(scheduleForConsignmentDTOS);
            scheduleToConfirmDTO.getDriverForScheduleDTOS().addAll(driverForScheduleDTO.mapToListResponse(drivers));
            scheduleToConfirmDTO.getVehicleForDetailDTOS().addAll(vehicleForDetailDTO.mapToListResponse(vehicles));
            if (total > 0) {
                ScheduleToConfirmDTO resultReturn = scheduleReturn(consignment, consignmentRequestDTO, scheduleToConfirmDTO.getScheduleForConsignmentDTOS(), driver_sub);
                if (resultReturn.getScheduleForConsignmentDTOS().size() > 0) {

                    scheduleToConfirmDTO.getScheduleForConsignmentDTOS().addAll(resultReturn.getScheduleForConsignmentDTOS());
                    scheduleToConfirmDTO.getDriverForScheduleDTOS().addAll(resultReturn.getDriverForScheduleDTOS());
                    scheduleToConfirmDTO.getVehicleForDetailDTOS().addAll(resultReturn.getVehicleForDetailDTOS());

                }
                if (resultReturn.getScheduleForConsignmentDTOS().size() == total) {
                    scheduleToConfirmDTO.setQuantity(0);
                } else if (resultReturn.getScheduleForConsignmentDTOS().size() < total) {
                    scheduleToConfirmDTO.setQuantity(total - resultReturn.getScheduleForConsignmentDTOS().size());
                }
            }

        }


        return scheduleToConfirmDTO;
    }

    private ScheduleToConfirmDTO scheduleReturn(Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO, List<ScheduleForConsignmentDTO> scheduleds, int driver_sub) throws ParseException {
        List<Vehicle> vehicles =
                vehicleService.findVehicleForSchedule(consignment, consignmentRequestDTO, ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue());
        int sizeVehicle = 0;
        for (int i = 0; i < consignmentRequestDTO.getVehicles().size(); i++) {
            String quantity = consignmentRequestDTO.getVehicles().get(i).getQuantity();
            sizeVehicle += Integer.parseInt(quantity);
        }
        List<Vehicle> vehiclesSave = new ArrayList<>();
        List<Driver> driversSave = new ArrayList<>();
        List<ScheduleToConfirmDTO> scheduleToConfirmDTOS = new ArrayList<>();
        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
        ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
        Schedule schedule = new Schedule();
        List<VehicleForDetailDTO> vehicleForDetailDTOS = new ArrayList<>();
        VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
        List<Driver> drivers = new ArrayList<>();
        if (vehicles.size() > 0 && vehicles.size() >= sizeVehicle) {
            Collections.sort(vehicles, new Comparator<Vehicle>() {
                @Override
                public int compare(Vehicle o1, Vehicle o2) {
                    return o1.getKilometerRunning().compareTo(o2.getKilometerRunning());
                }
            });

            Collections.sort(vehicles, new Comparator<Vehicle>() {
                @Override
                public int compare(Vehicle o1, Vehicle o2) {
                    return o1.getWeight().compareTo(o2.getWeight());
                }
            });
            for (int i = 0; i < sizeVehicle; i++) {
                if (scheduleds.size() > 0) {
                    for (int s = 0; s < scheduleds.size(); s++) {
                        if (vehicles.get(i).getId() != scheduleds.get(s).getVehicle().getId() && !vehiclesSave.contains(vehicles.get(i))) {
                            vehiclesSave.add(vehicles.get(i));
                        }
                    }

                } else {
                    if (!vehiclesSave.contains(vehicles.get(i))) {
                        vehiclesSave.add(vehicles.get(i));
                    }

                }

            }
//            scheduleToConfirmDTO.setVehicles(vehicleForDetailDTO.mapToListResponse(vehiclesSave));
            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTO.mapToListResponse(vehiclesSave));
        } else if (vehiclesSave.size() > 0 && vehiclesSave.size() < sizeVehicle) {
            vehicleForDetailDTOS = vehicleForDetailDTO.mapToListResponse(vehiclesSave);
            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTOS);

            // thieu xe
        }

///////////////////////////////////////////////////////////////////////
        List<Driver> resultDriver = new ArrayList<>();
        DriverForScheduleDTO driverForScheduleDTO = new DriverForScheduleDTO();
        if (vehicles.size() > 0 && vehicles.size() >= sizeVehicle) {
            for (int i = 0; i < vehicles.size(); i++) {


                resultDriver = driverService.findDriverForSchedule(vehicles.get(i).getWeight(), consignment);
                for (int j = 0; j < resultDriver.size(); j++) {
                    if (scheduleds.size() > 0) {
                        for (int s = 0; s < scheduleds.size(); s++) {
                            if (resultDriver.get(j).getId() != scheduleds.get(s).getDriver().getId() && !drivers.contains(resultDriver.get(j))) {
                                drivers.add(resultDriver.get(j));
                            }
                        }

                    } else {
                        if (!drivers.contains(resultDriver.get(j))) {
                            drivers.add(resultDriver.get(j));
                        }
                    }

                }


            }
        } else {

        }
        if (drivers.size() > 0 && drivers.size() >= sizeVehicle) {
            Collections.sort(drivers, (o1, o2) -> o1.getWorkingHour().compareTo(o2.getWorkingHour()));
            drivers.sort(new Comparator<Driver>() {
                @Override
                public int compare(Driver o1, Driver o2) {

                    if(o1.getSchedules().size()<o2.getSchedules().size()){
                        return -1;
                    }else if(o1.getSchedules().size()>o2.getSchedules().size()){
                        return 1;
                    }else{
                        return 0;
                    }

                }
            });
            for (int v = 0; v < vehiclesSave.size(); v++) {
                for (int k = 0; k < drivers.size(); k++) {
                    schedule = new Schedule();
                    if (!driversSave.contains(drivers.get(k))) {

                        int license = drivers.get(k).getDriverLicense();
                        if (license == 0 && vehiclesSave.get(v).getWeight() < 3.5) {
                            driversSave.add(drivers.get(k));
                            schedule.setConsignment(consignment);
                            schedule.setImageConsignment("no");
                            schedule.setId(null);
                            schedule.setDriver(drivers.get(k));
                            schedule.setVehicle(vehiclesSave.get(v));
                            schedule.setIsApprove(false);
//                            schedule = scheduleService.createSchedule(schedule);
                            if (schedule != null) {

                                scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
                            }
                            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
                            k = drivers.size();
                        } else if (license > 0) {
                            driversSave.add(drivers.get(k));
                            schedule.setConsignment(consignment);
                            schedule.setImageConsignment("no");
                            schedule.setId(null);
                            schedule.setDriver(drivers.get(k));
                            schedule.setVehicle(vehiclesSave.get(v));
                            schedule.setIsApprove(false);
//                            schedule = scheduleService.createSchedule(schedule);
                            if (schedule != null) {

                                scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
                            }
                            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
                            k = drivers.size();
                        }

                    }

                }
            }


        } else if (drivers.size() > 0 && drivers.size() <= sizeVehicle) {
            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
        }
        List<ScheduleForConsignmentDTO> list = new ArrayList<>();
        if (scheduleForConsignmentDTOS.size() > 0 && driver_sub == 2) {
            for (int i = 0; i < scheduleForConsignmentDTOS.size(); i++) {
                ScheduleForConsignmentDTO scheduleForConsignmentDTO = new ScheduleForConsignmentDTO();
                scheduleForConsignmentDTO = scheduleForConsignmentDTOS.get(i);
//            Schedule tmp = new Schedule();
                for (int t = 0; t < drivers.size(); t++) {
                    if (!driversSave.contains(drivers.get(t))) {
                        int license1 = drivers.get(t).getDriverLicense();
                        ScheduleForConsignmentDTO scheduleForConsignment = new ScheduleForConsignmentDTO();
//                        scheduleForConsignment = scheduleForConsignmentDTO;
                        if (license1 > 0) {
                            driversSave.add(drivers.get(t));
                            driverForScheduleDTO = new DriverForScheduleDTO();
                            driverForScheduleDTO = driverForScheduleDTO.convertToDto(drivers.get(t));
                            scheduleForConsignmentDTO.setInheritance(driverForScheduleDTO);
                            t = drivers.size();
                            sizeVehicle--;
//                            list.add(scheduleForConsignment);
                        } else if (license1 == 0 && scheduleForConsignmentDTO.getVehicle().getWeight() < 3.5) {
                            driversSave.add(drivers.get(t));
                            driverForScheduleDTO = new DriverForScheduleDTO();
                            driverForScheduleDTO = driverForScheduleDTO.convertToDto(drivers.get(t));
                            scheduleForConsignmentDTO.setInheritance(driverForScheduleDTO);
                            t = drivers.size();
                            sizeVehicle--;
//                            list.add(scheduleForConsignment);
                        }
                    }
                }
            }
            scheduleForConsignmentDTOS.addAll(list);
        }

        scheduleToConfirmDTO.setScheduleForConsignmentDTOS(scheduleForConsignmentDTOS);
        if (scheduleToConfirmDTO.getScheduleForConsignmentDTOS().size() == sizeVehicle) {
            scheduleToConfirmDTO.setQuantity(0);
        } else {
            scheduleToConfirmDTO.setQuantity(sizeVehicle - scheduleToConfirmDTO.getScheduleForConsignmentDTOS().size());
        }
//        scheduleToConfirmDTOS.add(scheduleToConfirmDTO);
        return scheduleToConfirmDTO;
    }

    @PostMapping("/status")
//    @MultipartConfig(maxFileSize =  @Value("${multipart.max-file-size}"), maxRequestSize = 1024*1024*1024)
    public ResponseEntity<ConsignmentResponseDTO> updateStatusSchedules(@RequestPart(value = "file") MultipartFile file, @ModelAttribute(value = "requestSaveScheObjDTO") String requestSaveScheObjDTO) {
        boolean result = false;

        JsonObject jsonObject = new JsonParser().parse(requestSaveScheObjDTO).getAsJsonObject();
        JsonArray jsonArray = (JsonArray) jsonObject.get("obejctScheDTOS");
        List<ObejctScheDTO> obejctScheDTOS = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject post_id = (JsonObject) jsonArray.get(i);
            ObejctScheDTO obejctScheDTO = new ObejctScheDTO();
            post_id.get("vehicle_id").getAsInt();
            obejctScheDTO.setVehicle_id(post_id.get("vehicle_id").getAsInt());
            obejctScheDTO.setDriver_id(post_id.get("driver_id").getAsInt());
            if (post_id.get("consignment_id") != null) {
                obejctScheDTO.setConsignment_id(post_id.get("consignment_id").getAsInt());
            }
            obejctScheDTOS.add(obejctScheDTO);
        }
        ConsignmentRequestDTO consignmentRequestDTO = new Gson().fromJson(jsonObject.get("consignmentRequestDTO"), ConsignmentRequestDTO.class);
//        List<ObejctScheDTO> obejctScheDTOS1 = new ArrayList<>();
//        obejctScheDTOS1 = obejctScheDTOS;
        RequestSaveScheObjDTO requestSaveScheObjDTO1 = new RequestSaveScheObjDTO();
        requestSaveScheObjDTO1.setConsignmentRequestDTO(consignmentRequestDTO);
        requestSaveScheObjDTO1.setObejctScheDTOS(obejctScheDTOS);
//        List<ObejctScheDTO> obejctScheDTOS = requestSaveScheObjDTO.getObejctScheDTOS();
        List<Schedule> schedules = new ArrayList<>();
        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        if (requestSaveScheObjDTO != null) {
            schedules = scheduleService.createSchedule(requestSaveScheObjDTO1, file);
            try {
                if (schedules.size() > 0) {
                    Consignment consignment = consignmentService.findById(schedules.get(0).getConsignment().getId());
                    consignmentResponseDTO = consignmentResponseDTO.mapToResponse(consignment);

                    // Save notification
                    NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
                    for (Schedule schedule : schedules) {
                        notificationRequestDTO.setVehicle_id(schedule.getVehicle().getId());
                        notificationRequestDTO.setDriver_id(schedule.getDriver().getId());
                        notificationRequestDTO.setStatus(false);
                        notificationRequestDTO.setContent("Bạn có lịch chạy mới của lô hàng #" + schedule.getConsignment().getId());
                        notificationRequestDTO.setType(NotificationTypeEnum.TASK_SCHEDULE.getValue());
                        notificationService.createNotification(notificationRequestDTO);
                    }
                    
                    return ResponseEntity.ok().body(consignmentResponseDTO);
                }

            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }

        }

        return ResponseEntity.noContent().build();
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
