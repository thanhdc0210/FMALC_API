package fmalc.api.dto;

import fmalc.api.entity.ReportIssue;
import fmalc.api.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportIssueResponseDTO{
    private String vehicleLicensePlates;
    private Map<Integer, ReportIssueContentResponseDTO> reportIssueContentResponses;

    public ReportIssueResponseDTO(Vehicle vehicle){
        vehicleLicensePlates = vehicle.getLicensePlates();
        Collection<ReportIssue> reportIssues = vehicle.getReportIssues();
        for(ReportIssue reportIssue : reportIssues){
            System.out.println(reportIssue.getContent());
        }
        if (reportIssueContentResponses == null){
            reportIssueContentResponses = new HashMap<>();
        }
        for(ReportIssue reportIssue : reportIssues){
            if (reportIssue.getStatus().equals(true)){
                ReportIssueContentResponseDTO reportIssueContentResponseDTO = new ReportIssueContentResponseDTO();
                reportIssueContentResponseDTO.setContent(reportIssue.getContent());
                reportIssueContentResponseDTO.setImageUrl(reportIssue.getImage());
                reportIssueContentResponseDTO.setInspectionId(reportIssue.getInspection().getId());
                reportIssueContentResponseDTO.setInspectionName(reportIssue.getInspection().getInspectionName());
                reportIssueContentResponses.put(reportIssue.getInspection().getId(), reportIssueContentResponseDTO);
            }
        }
    }
}
