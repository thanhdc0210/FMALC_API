package fmalc.api.service;

import fmalc.api.dto.*;
import fmalc.api.entity.Notification;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface NotificationService {
    Notification createNotification(NotificationRequestDTO notify) throws ParseException;

    NotificationUnread countNotificationUnread(String username);

    Paging getNotificationsByType(int type, String username, int page);

    void readNotification(String username, Integer notificationId);

    void readNotificationByType(String username, Integer type);

    Paging getNotificationsDayOff(String username,int page);
}
