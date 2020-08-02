package fmalc.api.service.impl;

import fmalc.api.dto.ReportIssueContentRequestDTO;
import fmalc.api.dto.ReportIssueDTO;
import fmalc.api.dto.ReportIssueInformationForUpdatingDTO;
import fmalc.api.dto.ReportIssueRequestDTO;
import fmalc.api.entity.ReportIssue;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.InspectionRepository;
import fmalc.api.repository.ReportIssueRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.ReportIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
    public boolean saveReportIssue(ReportIssueRequestDTO reportIssueRequestDTO) {

        // Danh sách report issue có sẵn trong DB
        ArrayList<ReportIssue> reportIssueList = reportIssueRepository.findByUsernameAndLicensePlates(
                reportIssueRequestDTO.getUsername(), reportIssueRequestDTO.getVehicleLicensePlates(),
                Timestamp.valueOf(LocalDateTime.now().with(LocalTime.MIN)), new Timestamp(System.currentTimeMillis()));

        if(reportIssueList == null || reportIssueList.isEmpty()) {

            // Create

            Map<Integer, ReportIssueContentRequestDTO> map = reportIssueRequestDTO.getReportIssueContentRequests();

            for (Map.Entry<Integer, ReportIssueContentRequestDTO> entry : map.entrySet()) {
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
                reportIssueList.add(reportIssue);
            }
            reportIssueRepository.saveAll(reportIssueList);
            return true;
        }else{

            // Update
            // Map truyền về
            Map<Integer, ReportIssueContentRequestDTO> map = reportIssueRequestDTO.getReportIssueContentRequests();
            // Tạo Arraylist để chứa value report issue truyền về
            ArrayList<ReportIssue> arrayList = new ArrayList<>();
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
                arrayList.add(reportIssue);
            }

            if (!reportIssueList.containsAll(arrayList)){
                for (int i=0; i<reportIssueList.size();i++){
                    for (int j=0; j<arrayList.size(); j++){
                        if (reportIssueList.get(i).getInspection().getId().equals(arrayList.get(j).getInspection().getId())){
                            // Gán giá trị mới cho clone list để update db
                            reportIssueList.get(i).setContent(arrayList.get(j).getContent());
                            reportIssueList.get(i).setImage(arrayList.get(j).getImage());
                            reportIssueList.get(i).setCreateTime(arrayList.get(j).getCreateTime());
                            // Gán id cho list report để biết report đó đã tồn tại. Chỉ update lại
                            arrayList.get(j).setId(reportIssueList.get(i).getId());
                        }
                    }
                }
            }

            Set<ReportIssue> set = new LinkedHashSet<>(reportIssueList);
            set.addAll(arrayList);
            List<ReportIssue> combinedList = new ArrayList<>(set);
//
//            Driver driver = driverRepository.findDriverByUsername(reportIssueRequestDTO.getUsername());
//            driver.setReportIssues(reportIssueList);
            for (ReportIssue reportIssue:combinedList){
                reportIssueRepository.save(reportIssue);
            }
            return true;
        }
    }

    @Override
    public boolean updateReportIssue(ReportIssueInformationForUpdatingDTO reportIssueInformationForUpdatingDTO) {
        boolean flag = false;
        String username = reportIssueInformationForUpdatingDTO.getUsername();
        List<Integer> reportIssueIdList = reportIssueInformationForUpdatingDTO.getReportIssueIdList();
        if (reportIssueIdList != null){
//            List<ReportIssue> reportIssues = reportIssueRepository.findAllById(reportIssueIdList);
            for (int i=0; i<reportIssueIdList.size(); i++){
                if (reportIssueRepository.existsById(reportIssueIdList.get(i))) {
                    ReportIssue reportIssue = reportIssueRepository.findById(reportIssueIdList.get(i)).get();
                    reportIssue.setId(reportIssueIdList.get(i));
                    reportIssue.setUpdatedBy(driverRepository.findDriverByUsername(username));
                    reportIssue.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    reportIssue.setStatus(false);
                    if (reportIssueRepository.save(reportIssue) != null) {
                        flag = true;
                    } else {
                        return false;
                    }
                }else{
                    return false;
                }
            }
        }
        return flag;
    }

    @Override
    public List<ReportIssueDTO> getReportIssueByVehicle(int idVehicle) {
        List<ReportIssue> reportIssues= reportIssueRepository.findReportIssueByVehicle(idVehicle);
        ReportIssueDTO reportIssueDTO = new ReportIssueDTO();
        List<ReportIssueDTO> reportIssueDTOS = reportIssueDTO.mapToListResponse(reportIssues);
        return reportIssueDTOS;
    }

    @Override
    public List<ReportIssue> getAllIssue() {
        return reportIssueRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
}
