package fmalc.api.controller;

import fmalc.api.dto.FuelTypeResponseDTO;
import fmalc.api.entity.FuelType;
import fmalc.api.entity.Vehicle;
import fmalc.api.service.FuelTypeService;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/fuel-types")
public class FuelTypeController {

    @Autowired
    FuelTypeService fuelTypeService;
    @Autowired
    VehicleService vehicleService;

    @GetMapping("/fuel-type")
    public ResponseEntity<FuelTypeResponseDTO> getFuelTypesAndVehicleLicensePlate(@RequestParam("status") List<Integer> status,
                                                                                  @RequestParam("username") String username){
        System.out.println(username);
        System.out.println(status.size());
        List<FuelType> fuelTypes = fuelTypeService.getListFuelType();
        Vehicle vehicle = vehicleService.findVehicleByUsernameAndConsignmentStatus(username, status);
        if (fuelTypes == null){
            return ResponseEntity.noContent().build();
        }else{
            if(vehicle == null){
                return ResponseEntity.noContent().build();
            }else{
                FuelTypeResponseDTO fuelTypeResponseDTO = new FuelTypeResponseDTO();
                fuelTypeResponseDTO.setVehicleLicensePlate(vehicle.getLicensePlates());
                fuelTypeResponseDTO.setFuelTypeList(fuelTypes);
                return ResponseEntity.ok().body(fuelTypeResponseDTO);
            }
        }
    }
}
