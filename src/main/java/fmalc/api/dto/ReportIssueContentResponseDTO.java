package fmalc.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportIssueContentResponseDTO{
    private Integer inspectionId;
    private String inspectionName;
    private String content;
    private String image;
    private Integer reportIssueId;
}
