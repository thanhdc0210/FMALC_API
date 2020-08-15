package fmalc.api.service;

import fmalc.api.dto.DayOffNotificationResponseDTO;
import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.dto.NotificationUnread;
import fmalc.api.entity.Notification;

import java.text.ParseException;
import java.util.List;

public interface NotificationService {
    Notification createNotification(NotificationRequestDTO notify) throws ParseException;

    NotificationUnread countNotificationUnread(String username);

    List<Notification> getNotificationsByType(int type);

    void readNotification(String username, Integer notificationId);

    void readNotificationByType(String username, Integer type);

    List<DayOffNotificationResponseDTO> getNotificationsDayOff();
}
