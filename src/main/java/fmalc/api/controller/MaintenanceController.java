package fmalc.api.controller;

import fmalc.api.dto.MaintainanceResponse;
import fmalc.api.entity.Maintenance;
import fmalc.api.service.MaintainanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/maintenances")
public class MaintenanceController {

    @Autowired
    MaintainanceService maintainanceService;

    @GetMapping
    public ResponseEntity getMaintenance() {
        try {
            List<Maintenance> maintenances = maintainanceService.getMaintenance();
            if (maintenances.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                List<MaintainanceResponse> result = new ArrayList<>(new MaintainanceResponse().mapToListResponse(maintenances));
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
