package fmalc.api.dto;

import lombok.Data;

@Data
public class InspectionDTO {
    private Integer id;
    private String inspectionName;
    private Boolean isActive;
}
