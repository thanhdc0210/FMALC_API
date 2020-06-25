package fmalc.api.dto;

import fmalc.api.entity.Vehicle;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class NotificationResponeDTO {
    private VehicleReponseDTO vehicle;
    private DriverResponseForNotifyDTO driver;
    private Timestamp time;
    private String content;
    private boolean status;
    private int id;
}
