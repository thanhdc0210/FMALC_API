package fmalc.api.dto;

import lombok.Data;

@Data
public class NotificationRequestDTO {
    private Integer vehicle_id;
    private Integer driver_id;
    private Boolean status;
    private String content;
    private Integer type;
}
