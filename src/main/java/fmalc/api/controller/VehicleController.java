package fmalc.api.controller;

import fmalc.api.dto.VehicleDTO;

import fmalc.api.entity.Vehicle;
import fmalc.api.entity.VehicleType;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
//@RequestMapping(name = "/api/v1.0/vehicles", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @GetMapping("/listVehicles")
    public ResponseEntity<List<Vehicle>> getLocationOfVehicle(){
        List<Vehicle> vehicles = vehicleService.getListVehicle();
        if(vehicles.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(vehicles);
    }

    @GetMapping("/detailVehicle/{id}")
    public ResponseEntity<Vehicle> getDetailVehicleById(@PathVariable int id){
        Vehicle vehicle = vehicleService.findVihicleById(id);
        if(vehicle == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(vehicle);
    }

    @PostMapping("/createVehicle")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody VehicleDTO dto) throws ParseException {
        Vehicle vehicle = new Vehicle();
        String dateString = dto.getDateOfManufacture();
        java.util.Date utilDate = new SimpleDateFormat("dd MM yyyy").parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        vehicle.setLicensePlates(dto.getLicensePlates());
        vehicle.setKilometerRunning(dto.getKilometerRunning());
        vehicle.setWeight(dto.getWeight());
        vehicle.setDateOfManufacture(sqlDate);
        VehicleType type = new VehicleType();
        type.setId(dto.getVehicleTypeId());
        vehicle.setVehicleType(type);
        vehicle =vehicleService.saveVehicle(vehicle);
        if(vehicle==null){
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(vehicle);

    }
}
