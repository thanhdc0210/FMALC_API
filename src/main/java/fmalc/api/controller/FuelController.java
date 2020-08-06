package fmalc.api.controller;


import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.Fuel;
import fmalc.api.service.FuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/fuels")
public class FuelController {

    @Autowired
    FuelService fuelService;

    @PostMapping("fuel-filling")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<Fuel> saveFuel(@RequestBody FuelRequestDTO fuelRequestDTO){
        Fuel fuel = fuelService.saveFuelFilling(fuelRequestDTO);
        if (fuel == null){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok().body(fuel);
        }
    }
}
