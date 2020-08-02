package fmalc.api.dto;

import fmalc.api.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMobileResponse {
    private Integer vehicleId;
    private Integer driverId;
    private Timestamp time;
    private String content;
    private boolean status;
    private Integer id;
    private Integer type;

    public NotificationMobileResponse mapToResponse(Notification notification) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(notification, NotificationMobileResponse.class);
    }

    public List<NotificationMobileResponse> mapToListResponse(List<Notification> notifications) {
        return notifications
                .stream()
                .map(notification -> mapToResponse(notification))
                .collect(Collectors.toList());
    }
}
