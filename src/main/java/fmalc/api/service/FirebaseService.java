package fmalc.api.service;

import fmalc.api.dto.NotificationRequest;

public interface FirebaseService {
    void sendPnsToDevice(NotificationRequest notificationRequest);
}
