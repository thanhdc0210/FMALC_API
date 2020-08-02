package fmalc.api.service;

import fmalc.api.entity.NotificationRequest;

public interface FirebaseService {
    public String sendPnsToDevice(NotificationRequest notificationRequest);
}
