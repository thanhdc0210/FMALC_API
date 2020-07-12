package fmalc.api.controller;

import fmalc.api.dto.ReportIssueRequestDTO;
import fmalc.api.entity.ReportIssue;
import fmalc.api.service.ReportIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/report-issues")
public class ReportIssueController {

    @Autowired
    ReportIssueService reportIssueService;

    @PostMapping
    public ResponseEntity<ReportIssueRequestDTO> createReportIssue(@RequestBody ReportIssueRequestDTO reportIssueRequestDTO){
        List<ReportIssue> reportIssues = reportIssueService.save(reportIssueRequestDTO);

        if (reportIssues == null){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok().body(reportIssueRequestDTO);
        }
    }
}
