package fmalc.api.dto;

import fmalc.api.entity.AccountNotification;
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
    private Timestamp time;
    private String content;
    private boolean status;
    private Integer type;

    public NotificationMobileResponse(AccountNotification accountNotification) {
        Notification notification = accountNotification.getNotification();
        this.time = notification.getTime();
        this.content = notification.getContent();
        this.status = accountNotification.getStatus();
        this.type = notification.getType();
    }

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
