package fmalc.api.controller;


import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.FuelHistory;
import fmalc.api.service.FuelHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0/fuelHistories")
public class FuelController {

    @Autowired
    FuelHistoryService fuelHistoryService;

    @PostMapping("fuel-filling")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<String> saveFuel(@RequestBody FuelRequestDTO fuelRequestDTO){
        FuelHistory fuelHistory = fuelHistoryService.saveFuelFilling(fuelRequestDTO);
        if (fuelHistory == null){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok().body("SUCCESS");
        }
    }
}
