package fmalc.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportIssueInformationForUpdatingDTO {
    private String username;
    private List<Integer> reportIssueIdList;
}
