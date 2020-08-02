package fmalc.api.service;

import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.dto.NotificationUnread;
import fmalc.api.entity.Notification;

import java.text.ParseException;
import java.util.List;

public interface NotificationService {
    Notification createNotification(NotificationRequestDTO notify) throws ParseException;

    NotificationUnread countNotificationUnread();

    List<Notification> getNotificationsByType(int type);

    List<Notification> findByDriverId(Integer driverId);

}
