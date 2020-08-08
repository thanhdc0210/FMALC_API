package fmalc.api.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class DayOffRequestDTO {
    private Date startDate;
    private Date endDate;
    private Integer fleetManagerId;
    private Integer driverId;
}
