package fmalc.api.controller;

import fmalc.api.dto.ReportIssueRequestDTO;
import fmalc.api.dto.ReportIssueResponseDTO;
import fmalc.api.entity.ReportIssue;
import fmalc.api.entity.Vehicle;
import fmalc.api.service.ReportIssueService;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1.0/report-issues")
public class ReportIssueController {

    @Autowired
    ReportIssueService reportIssueService;
    @Autowired
    VehicleService vehicleService;

    @PostMapping(value = "report")
    public ResponseEntity<ReportIssueRequestDTO> createReportIssue(@RequestBody ReportIssueRequestDTO reportIssueRequestDTO){
        boolean result = reportIssueService.save(reportIssueRequestDTO);

        if (result == false){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok().body(reportIssueRequestDTO);
        }
    }

    @GetMapping(value = "information")
    public ResponseEntity<ReportIssueResponseDTO> getIssueInformationOfAVehicle(@RequestParam(value = "username") String username){

        Vehicle vehicle = vehicleService.findVehicleByUsernameAndTime(username, Timestamp.valueOf(LocalDateTime.now().with(LocalTime.MIN)), new Timestamp(System.currentTimeMillis()));

        if (vehicle == null){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok().body(new ReportIssueResponseDTO(vehicle));
        }
    }
}
