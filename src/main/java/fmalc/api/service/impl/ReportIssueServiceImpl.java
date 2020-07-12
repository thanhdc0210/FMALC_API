package fmalc.api.service.impl;

import fmalc.api.dto.ReportIssueContentRequestDTO;
import fmalc.api.dto.ReportIssueRequestDTO;
import fmalc.api.entity.ReportIssue;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.InspectionRepository;
import fmalc.api.repository.ReportIssueRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.ReportIssueService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportIssueServiceImpl implements ReportIssueService {

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    InspectionRepository inspectionRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    ReportIssueRepository reportIssueRepository;

    @Override
    public List<ReportIssue> save(ReportIssueRequestDTO reportIssueRequestDTO) {

        List<ReportIssue> reportIssues = new ArrayList<>();

        Map<Integer, ReportIssueContentRequestDTO> map = reportIssueRequestDTO.getReportIssueContentRequests();

        for (Map.Entry<Integer, ReportIssueContentRequestDTO> entry : map.entrySet()){
            ReportIssue reportIssue = new ReportIssue();
            reportIssue.setCreatedBy(driverRepository.findDriverByUsername(reportIssueRequestDTO.getUsername()));
            reportIssue.setVehicle(vehicleRepository.findByLicensePlates(reportIssueRequestDTO.getVehicleLicensePlates()));
            ReportIssueContentRequestDTO reportIssueContentRequestDTO = entry.getValue();
            reportIssue.setContent(reportIssueContentRequestDTO.getContent());
            reportIssue.setImage(reportIssueContentRequestDTO.getImageUrl());
            reportIssue.setCreateTime(new Timestamp(System.currentTimeMillis()));
            reportIssue.setInspection(inspectionRepository.findById(reportIssueContentRequestDTO.getInspectionId()).get());
            reportIssue.setStatus(true);
            reportIssue.setType(reportIssueRequestDTO.getType());
            reportIssues.add(reportIssue);
        }

        return reportIssueRepository.saveAll(reportIssues);
    }
}
