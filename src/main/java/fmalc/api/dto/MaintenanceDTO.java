package fmalc.api.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class MaintenanceDTO {
    private Integer id;
    private int vehicle;
    private int driver;
    private String maintenanceType;
    private Date plannedMaintainDate;
    private Date actualMaintainDate;
}
