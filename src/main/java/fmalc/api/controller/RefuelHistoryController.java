package fmalc.api.controller;


import fmalc.api.dto.FuelRequestDTO;
import fmalc.api.entity.RefuelHistory;
import fmalc.api.service.RefuelHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0/fuelHistories")
public class RefuelHistoryController {

    @Autowired
    RefuelHistoryService refuelHistoryService;

    @PostMapping("fuel-filling")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<String> saveFuel(@RequestBody FuelRequestDTO fuelRequestDTO){
        RefuelHistory refuelHistory = refuelHistoryService.saveFuelFilling(fuelRequestDTO);
        if (refuelHistory == null){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok().body("SUCCESS");
        }
    }
}
