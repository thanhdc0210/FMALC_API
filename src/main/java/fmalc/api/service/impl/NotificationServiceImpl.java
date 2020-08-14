package fmalc.api.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.dto.NotificationResponeDTO;
import fmalc.api.dto.NotificationUnread;
import fmalc.api.entity.*;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.repository.*;
import fmalc.api.service.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountNotificationRepository accountNotificationRepository;

    private Logger logger = Logger.getLogger("MYLOG");

    @Override
//    @Transactional
    public Notification createNotification(NotificationRequestDTO dto) throws ParseException {
        Notification notify = convertToDto(dto);
        notify.setTime(new Timestamp(System.currentTimeMillis()));
        if (dto.getType() == NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue() ||
                dto.getType() == NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue()) {
            notify.setVehicle(null);
        } else {
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicle_id()).get();
            notify.setVehicle(vehicle);
        }
        notify.setDriver(driverRepository.findById(dto.getDriver_id()).get());
        notify.setType(dto.getType());
        notify.setContent(dto.getContent());
        notify.setDriver(driverRepository.findById(dto.getDriver_id()).get());

        try {
            Notification notification = notificationRepository.save(notify);

            if (notification != null) {

                Account account = accountRepository.findByDriverId(dto.getDriver_id());
                AccountNotificationKey accountNotificationKey = new AccountNotificationKey(account.getId(), notification.getId());
                AccountNotification accountNotification = accountNotificationRepository.save(AccountNotification.builder()
                        .id(accountNotificationKey)
                        .account(account)
                        .notification(notification)
                        .status(false)
                        .build());
                // Send notification to android
                if (accountNotification != null) {
                    if (dto.getType() != NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue() &&
                            dto.getType() != NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue()) {
                        String title = NotificationTypeEnum.getValueEnumToShow(dto.getType());
                        String content = dto.getContent();
                        Message message = Message.builder()
                                .setToken(driverRepository.findTokenDeviceByDriverId(dto.getDriver_id()))
                                .setNotification(new com.google.firebase.messaging.Notification(title, content))
                                .putData("title", title)
                                .putData("body", content)
                                .build();

                        String response = null;
                        try {
                            response = FirebaseMessaging.getInstance().send(message);
                        } catch (FirebaseMessagingException e) {
                            e.printStackTrace();
                            logger.info("Fail to send firebase notification " + e.getMessage());
                        }
                    }
                }

                return notification;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public NotificationUnread countNotificationUnread(String username) {
        NotificationUnread result = new NotificationUnread();
        result.setCount(accountNotificationRepository.countAllByAccount_UsernameAndStatusIsFalseAndNotification_TypeNot(username, 3));
        List<Notification> notifications = new ArrayList<>();
        List<AccountNotification> accountNotifications = accountNotificationRepository.findTop4ByAccount_UsernameAndStatusIsFalseAndNotification_TypeNot(username, 3);
        accountNotifications.stream().forEach(x -> notifications.add(x.getNotification()));
        result.setNotificationsUnread(new NotificationResponeDTO().mapToListResponse(notifications));
        return result;
    }

    @Override
    public List<Notification> getNotificationsByType(int type) {
        return notificationRepository.findAllByTypeOrderByIdDesc(type);
    }

//    public List<Notification> findByDriverId(Integer driverId) {
//        return notificationRepository.findByDriverId(driverId);
//    }

    @Override
    public void readNotification(String username, Integer notificationId) {
        AccountNotification accountNotification = accountNotificationRepository.findByNotification_IdAndAccount_Username(notificationId, username);
        accountNotification.setStatus(true);
        accountNotificationRepository.save(accountNotification);
    }

    @Override
    public void readNotificationByType(String username, Integer type) {
        List<AccountNotification> notifications = accountNotificationRepository.findAllByAccount_UsernameAndStatusIsFalseAndNotification_Type(username, type);
        notifications.stream().forEach(x -> x.setStatus(true));
        accountNotificationRepository.saveAll(notifications);
    }

    @Override
    public List<Notification> getNotificationsDayOff() {
        return notificationRepository.findAllByTypeInOrderByIdDesc(Arrays.asList(4, 5));
    }

    private Notification convertToDto(NotificationRequestDTO notificationRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Notification dto = modelMapper.map(notificationRequestDTO, Notification.class);
        return dto;
    }
}
