package fmalc.api.dto;

import fmalc.api.entity.Vehicle;
import lombok.Data;

@Data
public class NotificationResponeDTO {
    private VehicleReponseDTO vehicle;
    private NotificationTypeDTO notifyType;
    private String time;
    private String content;
}
