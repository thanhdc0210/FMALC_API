package fmalc.api.dto;

import lombok.Data;

@Data
public class ScheduleForDetailDTO {
    private Integer id;
    private VehicleForDetailDTO vehicle;
    private DriverForDetailDTO driver;
    private boolean isApprove;
    private String imageConsignment;
    private String note;
}
