package fmalc.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificationUnread {
    int count;
    List<NotificationResponeDTO> notificationsUnread;
}
