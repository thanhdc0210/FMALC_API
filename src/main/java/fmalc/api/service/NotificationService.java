package fmalc.api.service;

import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.entity.Notify;

import java.text.ParseException;

public interface NotificationService {
    Notify createNotifiation(NotificationRequestDTO notify) throws ParseException;
}
