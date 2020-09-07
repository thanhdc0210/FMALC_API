package fmalc.api.service;

import fmalc.api.dto.Paging;
import fmalc.api.dto.ReportIssueDTO;
import fmalc.api.dto.ReportIssueInformationForUpdatingDTO;
import fmalc.api.dto.ReportIssueRequestDTO;
import fmalc.api.entity.ReportIssue;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportIssueService {
    boolean saveReportIssue(ReportIssueRequestDTO reportIssueRequestDTO);
    boolean updateReportIssue(ReportIssueInformationForUpdatingDTO reportIssueInformationForUpdatingDTO);
//    List<ReportIssue> save(ReportIssueRequestDTO reportIssueRequestDTO);

    List<ReportIssueDTO> getReportIssueByVehicle(int idVehicle);

    Paging getAllIssue(String username,int page);
}
