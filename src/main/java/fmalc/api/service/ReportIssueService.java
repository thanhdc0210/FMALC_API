package fmalc.api.service;

import fmalc.api.dto.ReportIssueRequestDTO;
import fmalc.api.entity.ReportIssue;

import java.util.List;

public interface ReportIssueService {
    List<ReportIssue> save(ReportIssueRequestDTO reportIssueRequestDTO);
}
