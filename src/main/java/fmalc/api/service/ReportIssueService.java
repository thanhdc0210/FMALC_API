package fmalc.api.service;

import fmalc.api.dto.ReportIssueInformationForUpdatingDTO;
import fmalc.api.dto.ReportIssueRequestDTO;
import fmalc.api.dto.ReportIssueResponseDTO;
import fmalc.api.entity.ReportIssue;
import fmalc.api.entity.Vehicle;

import java.sql.Timestamp;
import java.util.List;

public interface ReportIssueService {
    boolean save(ReportIssueRequestDTO reportIssueRequestDTO);
    void update(ReportIssueInformationForUpdatingDTO reportIssueInformationForUpdatingDTO);
}
