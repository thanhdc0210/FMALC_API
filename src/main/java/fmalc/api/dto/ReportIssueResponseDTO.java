package fmalc.api.dto;

import fmalc.api.entity.ReportIssue;
import fmalc.api.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportIssueResponseDTO{
    private String vehicleLicensePlates;
    private List<ReportIssueContentResponseDTO> reportIssueContentResponses;

    public ReportIssueResponseDTO(Vehicle vehicle){
        vehicleLicensePlates = vehicle.getLicensePlates();
        Collection<ReportIssue> reportIssues = vehicle.getReportIssues();
        if (reportIssueContentResponses == null){
            reportIssueContentResponses = new ArrayList<>();
        }
        for(ReportIssue reportIssue : reportIssues){
            if (reportIssue.getStatus().equals(true)){
                ReportIssueContentResponseDTO reportIssueContentResponseDTO = new ReportIssueContentResponseDTO();
                reportIssueContentResponseDTO.setContent(reportIssue.getContent());
                reportIssueContentResponseDTO.setImage(reportIssue.getImage());
                reportIssueContentResponseDTO.setInspectionId(reportIssue.getInspection().getId());
                reportIssueContentResponseDTO.setInspectionName(reportIssue.getInspection().getInspectionName());
                reportIssueContentResponseDTO.setReportIssueId(reportIssue.getId());
                reportIssueContentResponses.add(reportIssueContentResponseDTO);
            }
        }
    }
}
