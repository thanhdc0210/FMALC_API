package fmalc.api.controller;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.DriverStatusEnum;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1.0/consignments")
public class ConsignmentController {

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    MaintainanceService maintainanceService;

    @Autowired
    PlaceService placeService;


    @GetMapping("test")
    public ResponseEntity<List<ScheduleForLocationDTO>> test(){
        List<ScheduleForLocationDTO> scheduleForLocationDTOS= scheduleService.getScheduleToCheck();
//        placeResponeDTOs = placeService.getPlaceOfConsignment(id);
        return ResponseEntity.ok().body(scheduleForLocationDTOS);
    }

    @GetMapping(value = "driver")
    public ResponseEntity<List<ConsignmentDTO>> findByConsignmentStatusAndUsernameForDriver(@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username) {
        List<Consignment> consignments = consignmentService.findByConsignmentStatusAndUsernameForDriver(status, username);

        if (consignments == null) {
            return ResponseEntity.noContent().build();
        }
        List<ConsignmentDTO> consignmentResponses = new ArrayList<>(new ConsignmentDTO().mapToListResponse(consignments));

        return ResponseEntity.ok().body(consignmentResponses);
    }

    @GetMapping(value = "fleetManager")
    public ResponseEntity<List<ConsignmentDTO>> findByConsignmentStatusAndUsernameForFleetManager(@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username) {
        List<Consignment> consignments = consignmentService.findByConsignmentStatusAndUsernameForFleetManager(status, username);

        if (consignments == null) {
            return ResponseEntity.noContent().build();
        }
        List<ConsignmentDTO> consignmentResponses = new ArrayList<>(new ConsignmentDTO().mapToListResponse(consignments));
        System.out.println(consignmentResponses.size());
        return ResponseEntity.ok().body(consignmentResponses);
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<DetailedConsignmentDTO> findById(@PathVariable("id") Integer id) {
        Consignment consignment = consignmentService.findById(id);
        if (consignment == null || consignment.equals("")) {
            return ResponseEntity.noContent().build();
        }
        DetailedConsignmentDTO detailedConsignmentDTO = new DetailedConsignmentDTO(consignment);

        return ResponseEntity.ok().body(detailedConsignmentDTO);
    }

    @GetMapping(value = "status")
    public ResponseEntity<List<ConsignmentListDTO>> getAllByStatus(@RequestParam("status") Integer status) {
        List<Consignment> consignments = consignmentService.getAllByStatus(status);
        ConsignmentListDTO consignmentListDTO = new ConsignmentListDTO();
        List<ConsignmentListDTO> consignmentListDTOS = consignmentListDTO.mapToListResponse(consignments);
        if (consignments.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }


        for (int i = 0; i < consignmentListDTOS.size(); i++) {
            List<ScheduleForLocationDTO> schedules = new ArrayList<>();
            schedules = scheduleService.getScheduleByConsignmentId(consignmentListDTOS.get(i).getId());
            if (schedules.size() > 0) {
                for (int j = 0; j < schedules.size(); j++) {
                    if (schedules.get(j).isApprove()) {
                        VehicleForDetailDTO vehicleForDetailDTO;
                        List<VehicleForDetailDTO> vehicleForDetailDTOS = new ArrayList<>();

                        Driver driver = new Driver();
                        List<Driver> drivers = new ArrayList<>();

                        DriverResponseDTO driverResponseDTO = new DriverResponseDTO();
                        List<DriverResponseDTO> driverResponseDTOS = new ArrayList<>();

                        vehicleForDetailDTO = vehicleService.findVehicleById(schedules.get(j).getVehicle_id());
                        vehicleForDetailDTOS.add(vehicleForDetailDTO);

                        driver = driverService.findById(schedules.get(j).getDriver_id());
                        drivers.add(driver);
                        driverResponseDTOS = driverResponseDTO.mapToListResponse(drivers);
                        consignmentListDTOS.get(i).setDrivers(driverResponseDTOS);
                        consignmentListDTOS.get(i).setVehicles(vehicleForDetailDTOS);
                    }

                }
            }
        }
        if (consignments.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(consignmentListDTOS);
    }

    @PostMapping
    public ResponseEntity<List<ScheduleToConfirmDTO>> createConsignment(@RequestBody ConsignmentRequestDTO consignmentRequestDTO) {
        try {
            Consignment consignment = new Consignment();
//            ScheduleForConsignment scheduleForConsignment = new ScheduleForConsignment();
            ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
            List<ScheduleToConfirmDTO> scheduleToConfirmDTOS = new ArrayList<>();
            consignmentRequestDTO.setImageConsignment("sdsaas");
            consignment = consignmentService.save(consignmentRequestDTO);
            List<Vehicle> vehicles = scheduleService.findVehicleForSchedule(consignment, consignmentRequestDTO);
            List<Driver> drivers = new ArrayList<>();
            Schedule schedule = new Schedule();
            if (vehicles.size() > 0) {
                Vehicle vehicle = vehicleService.getVehicleByKmRunning(vehicles);
                drivers = scheduleService.findDriverForSchedule(vehicle, consignment);
                if (drivers.size() > 0) {
                    Driver driver = Collections.min(drivers, Comparator.comparing(s -> s.getWorkingHour()));
                    schedule.setConsignment(consignment);
                    schedule.setDriver(driver);
                    schedule.setVehicle(vehicle);
                    schedule.setImageConsignment("no");
                    schedule.setNote("khong co");
                    schedule.setId(null);
                    schedule.setIsApprove(false);
                    schedule = scheduleService.createSchedule(schedule);
                    if (schedule != null) {
                        VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
                        List<VehicleForDetailDTO> vehicleForDetailDTOS = vehicleForDetailDTO.mapToListResponse(vehicles);
                        DriverForScheduleDTO driverForScheduleDTO = new DriverForScheduleDTO();
                        List<DriverForScheduleDTO> driverForScheduleDTOS = driverForScheduleDTO.mapToListResponse(drivers);
                        vehicleForDetailDTO = vehicleForDetailDTO.convertToDto(vehicle);
                        driverForScheduleDTO = driverForScheduleDTO.convertToDto(driver);
                        scheduleToConfirmDTO = scheduleToConfirmDTO.convertSchedule(schedule);
                        scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTOS);
                        scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTOS);
                        scheduleToConfirmDTO.setVehicle(vehicleForDetailDTO);
                        scheduleToConfirmDTO.setDriver(driverForScheduleDTO);
                        scheduleToConfirmDTOS.add(scheduleToConfirmDTO);
                        vehicleService.updateStatus(VehicleStatusEnum.SCHEDULED.getValue(), vehicle.getId());
                        driverService.updateStatus(DriverStatusEnum.SCHEDULED.getValue(), driver.getId());
                    } else {

                    }
                } else {
//                    return ResponseEntity.badRequest().body("Lô hàng đã được tạo nhưng không có tài xế phù hợp. Vui lòng thêm tài xế sau");
                }
            } else {
//                return ResponseEntity.badRequest().body("Lô hàng đã được tạo nhưng không có xe phù hợp. Vui lòng thêm xe sau");
            }


            return ResponseEntity.ok().body(scheduleToConfirmDTOS);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
