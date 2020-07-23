package fmalc.api.dto;

import lombok.Data;


import javax.persistence.Column;
import java.sql.Timestamp;

@Data
public class PlaceRequestDTO {
    private Double latitude;
    private Double longitude;
    private String address;
    private String name;
    private Timestamp plannedTime;
    private Timestamp actualTime;
    private Integer type;
    private String contactName;
    private String contactPhone;
    private Integer priority;
}
