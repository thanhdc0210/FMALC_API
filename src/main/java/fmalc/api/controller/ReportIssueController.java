package fmalc.api.controller;

import fmalc.api.dto.ReportIssueDTO;
import fmalc.api.dto.ReportIssueRequestDTO;
import fmalc.api.entity.ReportIssue;
import fmalc.api.service.ReportIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/report-issues")
public class ReportIssueController {

    @Autowired
    ReportIssueService reportIssueService;

    @PostMapping(value = "report")
    public ResponseEntity<ReportIssueRequestDTO> createReportIssue(@RequestBody ReportIssueRequestDTO reportIssueRequestDTO){
        List<ReportIssue> reportIssues = reportIssueService.save(reportIssueRequestDTO);

        if (reportIssues == null){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok().body(reportIssueRequestDTO);
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
