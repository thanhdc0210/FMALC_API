package fmalc.api.dto;

import fmalc.api.entity.Notification;
import fmalc.api.enums.NotificationTypeEnum;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NotificationResponeDTO {
    private VehicleResponseDTO vehicle;
    private DriverResponseForNotifyDTO driver;
    private Timestamp time;
    private String content;
    private boolean status;
    private int id;
    private int type;
    private String typeString;
    private String username;
    public NotificationResponeDTO mapToResponse(Notification notification) {
        ModelMapper modelMapper = new ModelMapper();
        this.typeString = NotificationTypeEnum.getValueEnumToShow(type);
        return modelMapper.map(notification, NotificationResponeDTO.class);
    }

    public List<NotificationResponeDTO> mapToListResponse(List<Notification> notifications) {
        return notifications
                .stream()
                .map(notification -> mapToResponse(notification))
                .collect(Collectors.toList());
    }
}
