package fmalc.api.dto;

import lombok.Data;

@Data
public class NotificationRequestDTO {
    private int vehicle_id;
    private int notify_type_id;
    private String time;
    private String content;
}
