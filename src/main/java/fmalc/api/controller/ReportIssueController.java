package fmalc.api.controller;

import fmalc.api.dto.ReportIssueDTO;
import fmalc.api.dto.ReportIssueInformationForUpdatingDTO;
import fmalc.api.dto.ReportIssueRequestDTO;
import fmalc.api.dto.ReportIssueResponseDTO;
import fmalc.api.entity.Vehicle;
import fmalc.api.service.ReportIssueService;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/report-issues")
public class ReportIssueController {

    @Autowired
    ReportIssueService reportIssueService;
    @Autowired
    VehicleService vehicleService;

    @PostMapping(value = "report-issue")
    public ResponseEntity<ReportIssueRequestDTO> createReportIssue(@RequestBody ReportIssueRequestDTO reportIssueRequestDTO){
        boolean result = reportIssueService.saveReportIssue(reportIssueRequestDTO);

        if (result == false){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok().body(reportIssueRequestDTO);
        }
    }

    // Lấy vehicle tài xế sắp chạy --> get report-issue của xe đó
    @GetMapping(value = "information-report-issue")
    public ResponseEntity<ReportIssueResponseDTO> getIssueInformationOfAVehicle(@RequestParam(value = "username") String username,
                                                                                @RequestParam(value = "status") List<Integer> status){
        try {

            Vehicle vehicle = vehicleService.findVehicleByUsernameAndConsignmentStatus(username, status,
                    Timestamp.valueOf(LocalDateTime.now().with(LocalTime.MIN)),
                    Timestamp.valueOf(LocalDateTime.now().with(LocalTime.MAX)));

            if (vehicle == null){
                return ResponseEntity.noContent().build();
            }else {
                return ResponseEntity.ok().body(new ReportIssueResponseDTO(vehicle));
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/report-issue")
    public ResponseEntity updateReportIssue(@RequestBody ReportIssueInformationForUpdatingDTO reportIssueInformationForUpdatingDTO) {
        boolean result = reportIssueService.updateReportIssue(reportIssueInformationForUpdatingDTO);
        if (result == false) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(reportIssueInformationForUpdatingDTO);
        }
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<List<ReportIssueDTO>> getIssueByIdVehicle(@PathVariable Integer id){
        List<ReportIssueDTO> reportIssues = reportIssueService.getReportIssueByVehicle(id);
        try{
            if(reportIssues.size()>0){
                return  ResponseEntity.ok().body(reportIssues);
            }else{
                return ResponseEntity.noContent().build();
            }
        }catch (Exception e){
            return ResponseEntity.noContent().build();
        }
    }
}
