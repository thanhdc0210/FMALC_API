package fmalc.api.controller;


import fmalc.api.dto.MaintenanceResponseDTO;
import fmalc.api.entity.Maintenance;
import fmalc.api.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/maintenances")
public class MaintenanceController {

    @Autowired
    MaintenanceService maintenanceService;

    @GetMapping("")
    public ResponseEntity<List<MaintenanceResponseDTO>> getListMaintenanceForDriver(@RequestParam Integer driverId) {
        List<Maintenance> maintenanceList = maintenanceService.getListMaintenanceForDriver(driverId);
        try {
            if (maintenanceList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                List<MaintenanceResponseDTO> result = new MaintenanceResponseDTO().mapToListResponse(maintenanceList);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping(value = "update-maintaining-complete")
    public ResponseEntity updateMaintainingComplete(@RequestParam("id") Integer id, @RequestParam("km") Integer km, @RequestPart(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Maintenance maintenance = maintenanceService.updateMaintainingComplete(id, km, file);
            if (maintenance != null) {
                return ResponseEntity.ok().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
