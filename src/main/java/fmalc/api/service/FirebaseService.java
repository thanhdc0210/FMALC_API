package fmalc.api.service;

import fmalc.api.dto.NotificationRequest;

public interface FirebaseService {
    public String sendPnsToDevice(NotificationRequest notificationRequest);
}
