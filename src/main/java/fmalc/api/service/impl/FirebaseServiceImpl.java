package fmalc.api.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import fmalc.api.entity.NotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.logging.Logger;

@Service
public class FirebaseServiceImpl {

    @Value("${app.firebase-config}")
    private String firebaseConfig;

    private FirebaseApp firebaseApp;

    private Logger logger = Logger.getLogger("MYLOG");

    @PostConstruct
    private void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfig).getInputStream())).build();

            if (FirebaseApp.getApps().isEmpty()) {
                this.firebaseApp = FirebaseApp.initializeApp(options);
            } else {
                this.firebaseApp = FirebaseApp.getInstance();
            }
        } catch (IOException e) {
            logger.info("Create FirebaseApp Error " + e.getMessage());
        }
    }

    public String sendPnsToDevice(NotificationRequest notificationRequest) {
        Message message = Message.builder()
                .setToken(notificationRequest.getTo())
                .setNotification(new Notification(notificationRequest.getNotificationData().getTitle(), notificationRequest.getNotificationData().getBody()))
                .putData("title", notificationRequest.getNotificationData().getTitle())
                .putData("body", notificationRequest.getNotificationData().getBody())
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            logger.info("Fail to send firebase notification " + e.getMessage());
        }

        return response;
    }
}
