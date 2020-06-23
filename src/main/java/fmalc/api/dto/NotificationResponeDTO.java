package fmalc.api.dto;

import fmalc.api.entity.Vehicle;
import lombok.Data;

@Data
public class NotificationResponeDTO {
    private VehicleReponseDTO vehicle;
    private DriverResponseDTO driver;
    private String time;
    private String content;
    private boolean status;
    private int id;
}
