package fmalc.api.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class InspectionDTO {
    private Integer id;
    private String inspectionName;
    private Boolean isActive;
}
