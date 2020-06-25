package fmalc.api.controller;


import fmalc.api.dto.LocationResponeDTO;
import fmalc.api.dto.VehicleForDetailDTO;

import fmalc.api.dto.VehicleReponseDTO;
import fmalc.api.entity.Location;
import fmalc.api.entity.Vehicle;

import fmalc.api.service.VehicleService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@RequestMapping(name = "/api/v1.0/vehicles", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @GetMapping("/listVehicles")
    public ResponseEntity<List<VehicleReponseDTO>> getLocationOfVehicle() {
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

    @PostMapping("/")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody VehicleForDetailDTO dto) throws ParseException {
        Vehicle vehicle = new Vehicle();

        String dateString = dto.getDateOfManufacture(); //

        java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        vehicle = convertToVehicleEntity(dto);
        vehicle.setStatus(dto.getStatus());
//        vehicle.setVehicleName(dto.getVehicleName());
//        vehicle.setLicensePlates(dto.getLicensePlates());
        vehicle.setKilometerRunning(0);
//        vehicle.setWeight(dto.getWeight());
        vehicle.setDateOfManufacture(sqlDate);
        vehicle.setDriverLicense(dto.getDriverLicense());


//        if(type== null){
//            type = vehicleTypeService.saveTypeVehicle()
//        }


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

    private Vehicle convertToVehicleEntity(VehicleForDetailDTO vehicleForDetailDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Vehicle vehicle = modelMapper.map(vehicleForDetailDTO, Vehicle.class);
        return vehicle;
    }

    @GetMapping(value = "/report-inspection")
    public ResponseEntity<List<String>> findVehicleLicensePlatesForReportInspection
            (@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username) {

        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        System.out.println(currentDate);

        List<String> vehiclePlates = vehicleService.findVehicleLicensePlatesForReportInspection(status, username, currentDate);

        if (vehiclePlates == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(vehiclePlates);

    }
}

