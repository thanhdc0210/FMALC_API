package fmalc.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportIssueContentRequestDTO {
    private String content;
    private String imageUrl;
}
