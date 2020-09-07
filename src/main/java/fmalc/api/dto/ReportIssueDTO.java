package fmalc.api.dto;

import fmalc.api.entity.ReportIssue;
import fmalc.api.enums.ReportIssueTypeEnum;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ReportIssueDTO {
    private Integer id;
    private VehicleForDetailDTO vehicle;
    private DriverResponseDTO createdBy;
    private DriverResponseDTO updatedBy;
    private InspectionDTO inspection;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String content;
    private Boolean status;
    private String image;
    private Integer type;
    private String typeStr;
    private int totalPage;
    public ReportIssueDTO convertSchedule(ReportIssue reportIssue){
        ModelMapper modelMapper = new ModelMapper();
        ReportIssueDTO reportIssueDTO = modelMapper.map(reportIssue, ReportIssueDTO.class);
        reportIssueDTO.setTypeStr(ReportIssueTypeEnum.getValueEnumToShow(reportIssueDTO.getType()));
        return  reportIssueDTO;
    }
    public List<ReportIssueDTO> mapToListResponse(List<ReportIssue> baseEntities) {
        List<ReportIssueDTO> result = baseEntities
                .stream()
                .map(reportIssue -> convertSchedule(reportIssue))
                .collect(Collectors.toList());
        return result;
    }
}
