package fmalc.api.controller;

import fmalc.api.dto.AlertRequestDTO;
import fmalc.api.dto.AlertResponseDTO;
import fmalc.api.entity.Alert;
import fmalc.api.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping
    public ResponseEntity<List<AlertResponseDTO>> getAllAlert() {
        List<Alert> alerts = alertService.getAlerts();
        return ResponseEntity.ok().body(new AlertResponseDTO().mapToListResponse(alerts));
    }

    @PostMapping("driver-send")
    public ResponseEntity<AlertRequestDTO> driverSendAlert(@RequestBody AlertRequestDTO alertRequest) {
        Alert alert = alertService.driverSendAlert(alertRequest);
        if (alert == null) {
            return ResponseEntity.noContent().build();
        }
        if (alert != null) {
            return ResponseEntity.ok().body(alertRequest);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}