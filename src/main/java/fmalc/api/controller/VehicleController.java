package fmalc.api.controller;

import fmalc.api.dto.VehicleForDetailDTO;

import fmalc.api.dto.VehicleReponseDTO;
import fmalc.api.entity.Vehicle;
import fmalc.api.entity.VehicleType;
import fmalc.api.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<VehicleReponseDTO>> getLocationOfVehicle(){
        List<Vehicle> vehicles = vehicleService.getListVehicle();

        if(vehicles.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<VehicleReponseDTO> vehicleDTOS =  vehicles.stream().map(this::convertToDto).collect(Collectors.toList());

        return ResponseEntity.ok().body(vehicleDTOS);
    }

    private VehicleReponseDTO convertToDto(Vehicle vehicleType) {
        ModelMapper modelMapper = new ModelMapper();
        VehicleReponseDTO dto = modelMapper.map(vehicleType, VehicleReponseDTO.class);

        return dto;
    }

    @GetMapping("/detailVehicle/{id}")
    public ResponseEntity<VehicleForDetailDTO> getDetailVehicleById(@PathVariable int id){
        VehicleForDetailDTO vehicle = vehicleService.findVehicleById(id);
        if(vehicle == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(vehicle);
    }

    @PostMapping("/")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody VehicleForDetailDTO dto) throws ParseException {
        Vehicle vehicle = new Vehicle();

        String dateString = dto.getDateOfManufacture();

        java.util.Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        vehicle.setStatus(dto.getStatus());
        vehicle.setVehicleName(dto.getVehicleName());
        vehicle.setLicensePlates(dto.getLicensePlates());
        vehicle.setKilometerRunning(dto.getKilometerRunning());
        vehicle.setWeight(dto.getWeight());
        vehicle.setDateOfManufacture(sqlDate);
        VehicleType type = new VehicleType();
        type.setId(dto.getVehicleType().getId());
        vehicle.setVehicleType(type);

        Vehicle checkLicensePlate = vehicleService.findVehicleByLicensePlates(dto.getLicensePlates());

        if(checkLicensePlate == null){
            System.out.println("VO day");
            vehicle =vehicleService.saveVehicle(vehicle);

            if(vehicle==null){
                return  ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(vehicle);
        }else{
            return  ResponseEntity.noContent().build();
        }



    }
}
