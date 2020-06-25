package fmalc.api.service;

import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.entity.Notification;


import java.text.ParseException;

public interface NotificationService {
    Notification createNotifiation(NotificationRequestDTO notify) throws ParseException;
}
