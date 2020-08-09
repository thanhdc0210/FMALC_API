package fmalc.api.controller;

import fmalc.api.dto.NotificationRequest;
import fmalc.api.service.impl.FirebaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1.0/fcm")
public class FirebaseCloudMessagingController {

    @Autowired
    FirebaseServiceImpl firebaseService;

    @PostMapping(value = "/device")
    public ResponseEntity<String> sendPnsToDevice(@RequestBody NotificationRequest notificationRequest) {
        return ResponseEntity.ok().body(firebaseService.sendPnsToDevice(notificationRequest));
    }
}
