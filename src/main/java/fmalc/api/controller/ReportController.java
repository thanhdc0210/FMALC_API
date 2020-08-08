package fmalc.api.controller;


import fmalc.api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1.0/reports")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/overview")
    public ResponseEntity<HashMap<String,Integer>> getOverviewReport() {
        HashMap<String,Integer> result = reportService.getOverviewReport();
        if (result.size() == 3){
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.noContent().build();

    }
}
