package fmalc.api.controller;


import fmalc.api.dto.ReportBySpecificRangeResponseDTO;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
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
    @GetMapping("/report-by-year")
    public ResponseEntity<HashMap<Integer,Object>> getReportByYear(@RequestParam("year") Integer year) throws ParseException {
        HashMap<Integer,Object> result = reportService.getReportByYear(year);
        if (result.size() == 12){
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/report-for-a-vehicle")
    public ResponseEntity<ReportBySpecificRangeResponseDTO> getReportForAVehicle(@RequestParam("id") Integer id,
                                    @RequestParam("startDate") String startDate,  @RequestParam("endDate") String endDate) throws ParseException {
        ReportBySpecificRangeResponseDTO result = reportService.getReportOneVehicleBySpecificRange(id, startDate, endDate, ConsignmentStatusEnum.COMPLETED.getValue());
        if (result!= null){
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.noContent().build();

    }


}
