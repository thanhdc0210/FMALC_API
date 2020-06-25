package fmalc.api.dto;

import lombok.Data;

@Data
public class NotificationRequestDTO {
    private int vehicle_id;
    private int driver_id;
    private String time;
    private boolean status;
    private String content;
}
