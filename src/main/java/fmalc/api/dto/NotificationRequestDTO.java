package fmalc.api.dto;

import lombok.Data;

@Data
public class NotificationRequestDTO {
    private int vehicle_id;
    private int driver_id;
    private boolean status;
    private String content;
    private int type;
}
