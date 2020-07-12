package fmalc.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportIssueRequestDTO {

    private String username;
    private String vehicleLicensePlates;
    private Map<Integer, ReportIssueContentRequestDTO> reportIssueContentRequests;
    private Integer type;
}
