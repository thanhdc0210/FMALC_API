package fmalc.api.dto;

import lombok.Data;

@Data
public class DayOffDriverRequestDTO {
    private String startDate;
    private String endDate;
    private String content;
    private Integer driverId;
    private Integer type;

}
