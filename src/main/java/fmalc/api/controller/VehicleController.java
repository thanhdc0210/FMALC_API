package fmalc.api.controller;


import fmalc.api.dto.*;

import fmalc.api.entity.Inspection;
import fmalc.api.entity.Vehicle;

import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.service.InspectionService;
import fmalc.api.service.VehicleService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1.0/vehicles")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @Autowired
    InspectionService inspectionService;

    private static int defaultKilometRunning = 0;

    @GetMapping("/listVehicles")
    public ResponseEntity<List<VehicleReponseDTO>> getListVehicle() {
        List<Vehicle> vehicles = vehicleService.getListVehicle();

        if (vehicles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<VehicleReponseDTO> vehicleDTOS = vehicles.stream().map(this::convertToDto).collect(Collectors.toList());

        return ResponseEntity.ok().body(vehicleDTOS);
    }

    private VehicleReponseDTO convertToDto(Vehicle vehicleType) {
        ModelMapper modelMapper = new ModelMapper();
        VehicleReponseDTO dto = modelMapper.map(vehicleType, VehicleReponseDTO.class);

        return dto;
    }

    @GetMapping("/detailVehicle/{id}")
    public ResponseEntity<VehicleForDetailDTO> getDetailVehicleById(@PathVariable int id) {
        VehicleForDetailDTO vehicle = vehicleService.findVehicleById(id);
        if (vehicle == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(vehicle);
    }

    @GetMapping("/detail/{licensePlates}")
    public ResponseEntity<VehicleForDetailDTO> getDetailVehicleByLicensePlates(@PathVariable String licensePlates) {
        Vehicle vehicle = vehicleService.findVehicleByLicensePlates(licensePlates);
        VehicleForDetailDTO vehicleForDetailDTO =new VehicleForDetailDTO();
        vehicleForDetailDTO = vehicleForDetailDTO.convertToDto(vehicle);
        if (vehicle == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(vehicleForDetailDTO);
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody VehicleForNewDTO dto) throws ParseException {
        Vehicle vehicle = new Vehicle();

        String dateString = dto.getDateOfManufacture(); //

        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        vehicle = convertToVehicleEntity(dto);
        vehicle.setStatus(VehicleStatusEnum.AVAILABLE.getValue());
        vehicle.setKilometerRunning(defaultKilometRunning);
        vehicle.setDateOfManufacture(sqlDate);
        vehicle.setDriverLicense(dto.getDriverLicense());

        Vehicle checkLicensePlate = vehicleService.findVehicleByLicensePlates(dto.getLicensePlates());

        if (checkLicensePlate == null) {

            vehicle = vehicleService.saveVehicle(vehicle);

            if (vehicle == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(vehicle);
        } else {
            return ResponseEntity.noContent().build();
        }


    }

    @PutMapping("/")
    public ResponseEntity<Vehicle> updateVehicle( @RequestBody VehicleForDetailDTO dto) throws ParseException {
        Vehicle vehicle = new Vehicle();
        VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
        vehicleForDetailDTO =dto;
        String dateString = dto.getDateOfManufacture(); //

        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        vehicle = vehicleForDetailDTO.convertToEnity(dto);
        vehicle.setStatus(VehicleStatusEnum.AVAILABLE.getValue());
        vehicle.setDateOfManufacture(sqlDate);
        vehicle.setDriverLicense(dto.getDriverLicense());

        Vehicle checkLicensePlate = vehicleService.findVehicleByLicensePlates(dto.getLicensePlates());


            vehicle = vehicleService.saveVehicle(vehicle);

            if (vehicle == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(vehicle);



    }

    private Vehicle convertToVehicleEntity(VehicleForNewDTO vehicleForNewDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Vehicle vehicle = modelMapper.map(vehicleForNewDTO, Vehicle.class);
        return vehicle;
    }



    @GetMapping(value = "/report-inspection-before-delivery")
    public ResponseEntity<InspectionResponseDTO> findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDelivery
            (@RequestParam(value = "username") String username) {

        String vehiclePlates = vehicleService.findVehicleLicensePlatesForReportInspectionBeforeDelivery(username
        , Timestamp.valueOf(LocalDateTime.now().with(LocalTime.MAX)) );
        List<Inspection> inspections = inspectionService.findAll();
        if (inspections == null){
            return ResponseEntity.noContent().build();
        }
        else {
            if (vehiclePlates == null) {
                InspectionResponseDTO inspectionResponseDTO = new InspectionResponseDTO();
                inspectionResponseDTO.setVehicleLicensePlates("");
                inspectionResponseDTO.setInspections(inspectionService.findAll());

                return ResponseEntity.ok().body(inspectionResponseDTO);
            }else {

                InspectionResponseDTO inspectionResponseDTO = new InspectionResponseDTO();
                inspectionResponseDTO.setVehicleLicensePlates(vehiclePlates);
                inspectionResponseDTO.setInspections(inspectionService.findAll());

                return ResponseEntity.ok().body(inspectionResponseDTO);
            }
        }
    }

    @GetMapping(value = "/report-inspection-after-delivery")
    public ResponseEntity<InspectionResponseDTO> findVehicleLicensePlatesAndInspectionForReportInspectionAfterDelivery
            (@RequestParam(value = "username") String username) {

        String vehiclePlates = vehicleService.findVehicleLicensePlatesForReportInspectionAfterDelivery(username, Timestamp.valueOf(LocalDateTime.now().with(LocalTime.MIN)));
        List<Inspection> inspections = inspectionService.findAll();
        if (inspections == null){
            return ResponseEntity.noContent().build();
        }
        else {
            if (vehiclePlates == null) {
                InspectionResponseDTO inspectionResponseDTO = new InspectionResponseDTO();
                inspectionResponseDTO.setVehicleLicensePlates("");
                inspectionResponseDTO.setInspections(inspectionService.findAll());

                return ResponseEntity.ok().body(inspectionResponseDTO);
            }else {

                InspectionResponseDTO inspectionResponseDTO = new InspectionResponseDTO();
                inspectionResponseDTO.setVehicleLicensePlates(vehiclePlates);
                inspectionResponseDTO.setInspections(inspectionService.findAll());

                return ResponseEntity.ok().body(inspectionResponseDTO);
            }
        }
    }
}

