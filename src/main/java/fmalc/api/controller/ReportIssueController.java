package fmalc.api.controller;

import fmalc.api.dto.*;
import fmalc.api.entity.ReportIssue;
import fmalc.api.entity.Vehicle;
import fmalc.api.service.ReportIssueService;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<ReportIssueRequestDTO> createReportIssue(@RequestBody ReportIssueRequestDTO reportIssueRequestDTO) {
        boolean result = reportIssueService.saveReportIssue(reportIssueRequestDTO);

        if (result == false) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(reportIssueRequestDTO);
        }
    }

    // Lấy vehicle tài xế sắp chạy --> get report-issue của xe đó
    @GetMapping(value = "information-report-issue")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<ReportIssueResponseDTO> getIssueInformationOfAVehicle(@RequestParam(value = "username") String username,
                                                                                @RequestParam(value = "status") List<Integer> status) {
        try {

            String licensePlate = vehicleService.findLicensePlatesForMakingReportBeforeRunningOrWhileRunning(status, username);
            Vehicle vehicle = vehicleService.findVehicleByLicensePlates(licensePlate);

            if (vehicle == null) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok().body(new ReportIssueResponseDTO(vehicle));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/report-issue")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity updateReportIssue(@RequestBody ReportIssueInformationForUpdatingDTO reportIssueInformationForUpdatingDTO) {
        try {
            boolean result = reportIssueService.updateReportIssue(reportIssueInformationForUpdatingDTO);
            if (result == false) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok().body(reportIssueInformationForUpdatingDTO);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<List<ReportIssueDTO>> getIssueByIdVehicle(@PathVariable Integer id) {
        List<ReportIssueDTO> reportIssues = reportIssueService.getReportIssueByVehicle(id);
        try {
            if (reportIssues.size() > 0) {
                return ResponseEntity.ok().body(reportIssues);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping()
    public ResponseEntity<Paging> getIssue(@RequestParam("username") String username,@RequestParam("page") int page) {
        Paging reportIssues = reportIssueService.getAllIssue(username,page);
        try {
            if (!reportIssues.getList().isEmpty()) {
                return ResponseEntity.ok().body(reportIssues);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
}
