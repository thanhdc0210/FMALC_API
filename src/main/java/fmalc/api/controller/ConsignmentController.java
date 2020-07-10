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
    public ResponseEntity< List<ScheduleForLocationDTO>> test() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Long plannedTime = finishPlace.getPlannedTime().getTime();
        String s ="";
        List<ScheduleForLocationDTO> scheduleForLocationDTOS = scheduleService.getScheduleToCheck();
        return ResponseEntity.ok().body(scheduleForLocationDTOS);
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
    public ResponseEntity<List<ScheduleForLocationDTO>> createConsignment(@RequestBody ConsignmentRequestDTO consignmentRequestDTO) {
        try {
            Consignment consignment = new Consignment();
//            ScheduleForConsignment scheduleForConsignment = new ScheduleForConsignment();
            ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
            List<ScheduleToConfirmDTO> scheduleToConfirmDTOS = new ArrayList<>();
            ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
            consignmentRequestDTO.setImageConsignment("sdsaas");
            consignment = consignmentService.save(consignmentRequestDTO);
            ScheduleForLocationDTO scheduleForLocationDTO = new ScheduleForLocationDTO();
            List<Vehicle> vehicles = scheduleService.findVehicleForSchedule(consignment, consignmentRequestDTO);
            List<ScheduleForLocationDTO> scheduleForLocationDTOS = scheduleService.findScheduleForFuture(vehicles, consignment, consignmentRequestDTO);
//            scheduleToConfirmDTOS = scheduleReturn(vehicles, consignment, consignmentRequestDTO);
            consignmentResponseDTO = consignmentResponseDTO.mapToResponse(consignment);
            scheduleForLocationDTO.setConsignment(consignmentResponseDTO);
            scheduleForLocationDTOS.add(scheduleForLocationDTO);
            if (consignmentRequestDTO.getVehicles().size() <= scheduleForLocationDTOS.size()) {

            } else {

            }

            return ResponseEntity.ok().body(scheduleForLocationDTOS);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    private List<ScheduleToConfirmDTO> scheduleReturn(List<Vehicle> vehicles, Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO) {
        int sizeVehicle = 0;
        for (int i = 0; i < consignmentRequestDTO.getVehicles().size(); i++) {
            String quantity = consignmentRequestDTO.getVehicles().get(i).getQuantity();
            sizeVehicle += Integer.parseInt(quantity);
        }
        List<Vehicle> vehiclesSave = new ArrayList<>();
        List<Driver> driversSave = new ArrayList<>();
        List<ScheduleToConfirmDTO> scheduleToConfirmDTOS = new ArrayList<>();
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
            for (int i = 0; i < consignmentRequestDTO.getVehicles().size(); i++) {
                vehiclesSave.add(vehicles.get(i));
            }
            scheduleToConfirmDTO.setVehicles(vehicleForDetailDTO.mapToListResponse(vehiclesSave));
            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTO.mapToListResponse(vehicles));
        } else if (vehicles.size() > 0 && vehicles.size() < sizeVehicle) {
            vehicleForDetailDTOS = vehicleForDetailDTO.mapToListResponse(vehicles);
            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTOS);
        }

///////////////////////////////////////////////////////////////////////
        DriverForScheduleDTO driverForScheduleDTO = new DriverForScheduleDTO();
        if (vehicles.size() > 0 && vehicles.size() >= sizeVehicle) {
            for (int i = 0; i < consignmentRequestDTO.getVehicles().size(); i++) {
                double weight = Double.parseDouble(consignmentRequestDTO.getVehicles().get(i).getWeight());
                drivers = scheduleService.findDriverForSchedule(weight, consignment);
            }
        } else {

        }
        if (drivers.size() > 0 && drivers.size() >= sizeVehicle) {
            Collections.sort(drivers, new Comparator<Driver>() {
                @Override
                public int compare(Driver o1, Driver o2) {
                    return o1.getWorkingHour().compareTo(o2.getWorkingHour());
                }
            });
            for (int i = 0; i < consignmentRequestDTO.getVehicles().size(); i++) {
                driversSave.add(drivers.get(i));
                schedule.setConsignment(consignment);
                schedule.setImageConsignment("no");
                schedule.setNote("khong co");
                schedule.setId(null);
                schedule.setDriver(drivers.get(i));
                schedule.setVehicle(vehiclesSave.get(i));
                schedule.setIsApprove(false);
                schedule = scheduleService.createSchedule(schedule);
                List<DriverForScheduleDTO> driverForScheduleDTOS = driverForScheduleDTO.mapToListResponse(drivers);
                scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTOS);
//            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTOS);
                scheduleToConfirmDTO.setDrivers(driverForScheduleDTO.mapToListResponse(driversSave));

                if (schedule != null) {
                    scheduleToConfirmDTO = scheduleToConfirmDTO.convertSchedule(schedule);
                    scheduleToConfirmDTOS.add(scheduleToConfirmDTO);
                    vehicleService.updateStatus(VehicleStatusEnum.SCHEDULED.getValue(), vehiclesSave.get(i).getId());
                    driverService.updateStatus(DriverStatusEnum.SCHEDULED.getValue(), driversSave.get(i).getId());
                } else {
                    scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
                }

            }


        } else if (drivers.size() > 0 && drivers.size() <= sizeVehicle) {
            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
        }
        scheduleToConfirmDTOS.add(scheduleToConfirmDTO);
        return scheduleToConfirmDTOS;
    }
}
