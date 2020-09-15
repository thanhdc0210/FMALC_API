package fmalc.api.controller;

import fmalc.api.dto.AlertRequestDTO;
import fmalc.api.dto.Paging;
import fmalc.api.entity.Alert;
import fmalc.api.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1.0/alerts")
public class AlertController {

    @Autowired
    AlertService alertService;

    @GetMapping
    public ResponseEntity<Paging> getAllAlert(@RequestParam("username") String username, @RequestParam("page") int page) {
        Paging alerts = alertService.getAlerts(username,page);
        return ResponseEntity.ok().body((alerts));
    }

    @PostMapping("driver-send")
    public ResponseEntity<AlertRequestDTO> driverSendAlert(@RequestBody AlertRequestDTO alertRequest) {
        Alert alert = alertService.driverSendAlert(alertRequest);
        try{
            if (alert != null) {
                return ResponseEntity.ok().body(alertRequest);
            } else {
                return ResponseEntity.noContent().build();
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("status/{id}")
    public ResponseEntity updateStatus(@PathVariable("id") int id) {
        try{
            Alert alerts = alertService.updateStatus(id);
            if(alerts.getStatus()){
                return ResponseEntity.ok().body(alerts);
            }else{
                return ResponseEntity.noContent().build();
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}