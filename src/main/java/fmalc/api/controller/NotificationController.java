package fmalc.api.controller;

import fmalc.api.dto.*;
import fmalc.api.entity.Account;

import fmalc.api.entity.AccountNotification;

import fmalc.api.entity.Driver;
import fmalc.api.entity.Notification;

import fmalc.api.entity.Alert;
import fmalc.api.entity.Notification;
import fmalc.api.enums.LevelInAlertEnum;
import fmalc.api.enums.NotificationTypeEnum;

import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1.0/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    AccountService accountService;

    ScheduleService scheduleService;


    @Autowired
    AccountNotificationService accountNotificationService;

    @Autowired
    AlertService alertService;

    // list notify
    private List<NotificationResponeDTO> notificationResponeDTOS = new ArrayList<>();

    // check notify new or old
    private NotificationResponeDTO notificationSend = new NotificationResponeDTO();

    // set 5s to do
    private Flux<Long> intervals = Flux.interval(Duration.ofSeconds(5));
    private Flux<List<NotificationResponeDTO>> flux;

    @PostMapping("/")
    public ResponseEntity<NotificationResponeDTO> createNotification(@RequestBody NotificationRequestDTO notificationRequestDTO) {
        NotificationResponeDTO notificationResponeDTO;
        try {

            Notification notificationSaved = notificationService.createNotification(notificationRequestDTO);
            if (notificationSaved != null) {

                Driver driver = driverService.findById(notificationSaved.getDriver().getId());
                Account account = accountService.findById(driver.getFleetManager().getAccount().getId());

                notificationResponeDTO = new NotificationResponeDTO().mapToResponse(notificationSaved);
                notificationResponeDTO.setUsername(account.getUsername());
                if (notificationSend != notificationResponeDTO) {
                    notificationResponeDTOS.add(notificationResponeDTO);
                    intervals.subscribe((i) -> notifyForManagerWorkingHours()).dispose();

                }

                return ResponseEntity.ok().body(notificationResponeDTO);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    private List<NotificationResponeDTO> returnResponeFor() {
        return notificationResponeDTOS;
    }
    @GetMapping(value = "/notificationworking", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<NotificationResponeDTO>> notifyForManagerWorkingHours() {
        System.out.println("EEE");
        Flux<List<NotificationResponeDTO>> monoTransaction = Flux.fromStream(Stream.generate(() -> returnResponeFor()));
        monoTransaction = Flux.fromStream(Stream.generate(() -> returnResponeFor()));
        if (returnResponeFor().size() > 0) {
            System.out.println(">0");
            intervals.subscribe((i) -> returnResponeFor());
            flux = Flux.zip(intervals, monoTransaction).map(Tuple2::getT2);
            notificationResponeDTOS = new ArrayList<>();
        } else {
            System.out.println(">0");
            intervals.subscribe((i) -> returnResponeFor()).dispose();
            flux = Flux.zip(intervals, monoTransaction).map(Tuple2::getT2);
        }
        return flux;
    }

    // delete list to disconnect notify
    @GetMapping(value = "/notificationworking/received", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<String> notifyReceived() {

//        closeInterval();
        notificationResponeDTOS = new ArrayList<>();
        String result = "OK";
        intervals.subscribe((i) -> notifyForManagerWorkingHours()).dispose();


        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/count-notification-unread")
    public NotificationUnread countNotificationUnread(@RequestParam("username") String username) {
        return notificationService.countNotificationUnread(username);
    }

    @GetMapping(value = "/by-type")
    public ResponseEntity getNotificationsByType(@RequestParam("type") int type) {
        List<Notification> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok().body(new NotificationResponeDTO().mapToListResponse(notifications));
    }

    @GetMapping(value = "/account/{username}")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<List<NotificationMobileResponse>> findNotificationByUsername(@PathVariable("username") String username) {

        try {

            List<AccountNotification> accountNotifications = accountNotificationService.findByUsername(username);
            if (accountNotifications != null) {
                List<NotificationMobileResponse> notificationMobileResponses = new ArrayList<>();


//                for (AccountNotification accountNotification : accountNotifications) {
//                    notificationMobileResponses.add(new NotificationMobileResponse(accountNotification));

                for(AccountNotification accountNotification : accountNotifications){
                    if (accountNotification.getNotification().getType().equals(NotificationTypeEnum.TASK_SCHEDULE.getValue())) {
                        NotificationMobileResponse notificationMobileResponse = new NotificationMobileResponse(accountNotification);
                        String subString[] = notificationMobileResponse.getContent().split("#");
                        String subStringId[] = subString[subString.length - 1].split("\\s");
                        Integer consignmentId =  Integer.valueOf(subStringId[0]);
                        notificationMobileResponse.setScheduleId(scheduleService.findScheduleIdByConsignmentIdAndDriverId(consignmentId, driverService.findDriverByUsername(notificationMobileResponse.getUsername()).getId()));
                        notificationMobileResponses.add(notificationMobileResponse);
                    }else{
                        notificationMobileResponses.add(new NotificationMobileResponse(accountNotification));
                    }

                }

                if (notificationMobileResponses != null) {
                    return ResponseEntity.ok().body(notificationMobileResponses);
                } else {
                    return ResponseEntity.noContent().build();
                }
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/read")
    public ResponseEntity readNotification(@RequestParam("username") String username,
                                           @RequestParam("notificationId") Integer notificationId) {
        try {
            notificationService.readNotification(username, notificationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/read-all-type")
    public ResponseEntity readNotificationByType(@RequestParam("username") String username,
                                                 @RequestParam("type") Integer type) {
        try {
            notificationService.readNotificationByType(username, type);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/notify-for-alert")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<NotificationResponeDTO> driverSendAlert(@RequestBody AlertRequestDTO alertRequest) throws ParseException {
        Alert alert = alertService.driverSendAlert(alertRequest);
        NotificationResponeDTO notificationResponeDTO;
        if (alert == null) {
            return ResponseEntity.noContent().build();
        }
        if (alert != null) {
            NotificationRequestDTO noti = new NotificationRequestDTO();
//            noti.setType(NotificationTypeEnum.ALERT.getValue());
            noti.setDriver_id(alert.getDriver().getId());
            noti.setStatus(false);
            noti.setVehicle_id(alert.getVehicle().getId());
            noti.setContent("Báo cáo từ tài xế loại " + LevelInAlertEnum.getValueEnumToShow(alert.getLevel()) + ":" + alert.getContent());
            try {
                Notification notificationSaved = notificationService.createNotification(noti);
                if (notificationSaved != null) {
                    notificationResponeDTO = new NotificationResponeDTO().mapToResponse(notificationSaved);
                    if (notificationSend != notificationResponeDTO) {
                        notificationResponeDTOS.add(notificationResponeDTO);
                        intervals.subscribe((i) -> notifyForManagerWorkingHours()).dispose();
                    }
                    return ResponseEntity.ok().body(notificationResponeDTO);
                } else {
                    return ResponseEntity.noContent().build();
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/dayoff")
    public ResponseEntity getNotificationsDayOff() {
        List<DayOffNotificationResponseDTO> notifications = notificationService.getNotificationsDayOff();
        return ResponseEntity.ok().body(notifications);
    }
}
