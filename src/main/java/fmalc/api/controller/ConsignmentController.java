package fmalc.api.controller;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.DriverStatusEnum;
import fmalc.api.enums.ScheduleConsginmentEnum;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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

    @Autowired
    ParkingService parkingService;


    @GetMapping("test")
    public ResponseEntity<List<ScheduleForLocationDTO>> test() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Long plannedTime = finishPlace.getPlannedTime().getTime();
        String s = "";
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
    public ResponseEntity<NewScheduleDTO> createConsignment(@RequestBody ConsignmentRequestDTO consignmentRequestDTO) {
        try {
            NewScheduleDTO newScheduleDTO = new NewScheduleDTO();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.getDefault());
            Consignment consignment = new Consignment();
            ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
            List<ScheduleToConfirmDTO> scheduleToConfirmDTOS = new ArrayList<>();
            ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
            consignmentRequestDTO.setImageConsignment("sdsaas");
            consignment = consignmentService.save(consignmentRequestDTO);
            ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
            List<Vehicle> vehicles =
                    scheduleService.findVehicleForSchedule(consignment, consignmentRequestDTO, ScheduleConsginmentEnum.SCHEDULE_NOT_CHECK.getValue());
            List<ScheduleForConsignmentDTO> scheduleForLocationDTOS =
                    scheduleService.findScheduleForFuture(vehicles, consignment, consignmentRequestDTO);
            for (int i = 0; i < scheduleForLocationDTOS.size(); i++) {
                List<PlaceResponeDTO> placeResponeDTOS = scheduleForLocationDTOS.get(i).getConsignment().getPlaces();
                for (int j = 0; j < placeResponeDTOS.size(); j++) {
                    String dateTemp = sdf.format(placeResponeDTOS.get(j).getPlannedTime());
                    dateTemp = dateTemp.replace("T", " ");
//                dateTemp = dateTemp.replace("+", "");
                    dateTemp = dateTemp.substring(0, dateTemp.indexOf("+"));
                    placeResponeDTOS.get(j).setPlannedTime(Timestamp.valueOf(dateTemp));
                }
                scheduleForLocationDTOS.get(i).getConsignment().setPlaces(placeResponeDTOS);
            }
            consignmentResponseDTO = consignmentResponseDTO.mapToResponse(consignment);
            for (int j = 0; j < consignmentResponseDTO.getPlaces().size(); j++) {
                String dateTemp = sdf.format(consignmentResponseDTO.getPlaces().get(j).getPlannedTime());
                dateTemp = dateTemp.replace("T", " ");
//                dateTemp = dateTemp.replace("+", "");
                dateTemp = dateTemp.substring(0, dateTemp.indexOf("+"));
                consignmentResponseDTO.getPlaces().get(j).setPlannedTime(Timestamp.valueOf(dateTemp));
            }

            scheduleForLocationDTO.setConsignment(consignmentResponseDTO);
            scheduleForLocationDTOS.add(scheduleForLocationDTO);
            if (consignmentRequestDTO.getVehicles().size() <= scheduleForLocationDTOS.size()) {

            } else {

            }

            ParkingDTO parkingDTO = parkingService.getParking();
            newScheduleDTO.setParkingDTO(parkingDTO);
            newScheduleDTO.setScheduleForConsignmentDTOS(scheduleForLocationDTOS);
            return ResponseEntity.ok().body(newScheduleDTO);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }


}
