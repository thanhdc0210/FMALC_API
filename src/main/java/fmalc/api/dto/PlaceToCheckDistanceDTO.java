package fmalc.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PlaceToCheckDistanceDTO {
    private Integer id;
    private Double latitude;
    private Double longitude;
    private String address;
    private String name;
    private Date plannedTime;
    private Date actualTime;
    private Integer type;
    private int priority;
}
