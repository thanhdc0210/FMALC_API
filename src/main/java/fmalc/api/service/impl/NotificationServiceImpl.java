package fmalc.api.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.entity.AccountNotification;
import fmalc.api.entity.Notification;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.repository.*;
import fmalc.api.service.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
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
    public Notification createNotification(NotificationRequestDTO dto) throws ParseException {
        Notification notify = convertToDto(dto);
        notify.setTime(new Timestamp(System.currentTimeMillis()));
        if(dto.getType() == NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue() ||
                dto.getType() == NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue()){
            notify.setVehicle(null);
        } else {
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicle_id()).get();
            notify.setVehicle(vehicle);
        }
        notify.setType(dto.getType());
        notify.setContent(dto.getContent());

        try {
            Notification notification = notificationRepository.save(notify);

            if (notification != null){

                AccountNotification accountNotification = new AccountNotification();
                accountNotification.setAccount(accountRepository.findByDriverId(dto.getDriver_id()));
                accountNotification.setNotification(notification);
                accountNotification.setStatus(false);

                if (accountNotificationRepository.save(accountNotification) != null){

                    // Send notification to android
                    if(dto.getType() != NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue() ||
                            dto.getType() != NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue()){
                        String title = NotificationTypeEnum.getValueEnumToShow(dto.getType());
                        String content  = dto.getContent();
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
                            logger.info("Fail to send firebase notification " + e.getMessage());
                        }
                    }

                    return notification;
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }catch (Exception e) {
            return null;
        }
    }

//    @Override
//    public NotificationUnread countNotificationUnread(String username) {
//        NotificationUnread result = new NotificationUnread();
//        Account account = accountRepository.findByUsername(username);
//        result.setCount(notificationRepository.countAllByAccountNotContainsAndTypeNot(account, 3));
//        result.setNotificationsUnread(new NotificationResponeDTO().mapToListResponse(notificationRepository.findTop4ByAccountNotContainsAndTypeNotOrderByIdDesc(account, 3)));
//        return result;
//    }

    @Override
    public List<Notification> getNotificationsByType(int type) {
        return notificationRepository.findAllByTypeOrderByIdDesc(type);
    }

//    public List<Notification> findByDriverId(Integer driverId) {
//        return notificationRepository.findByDriverId(driverId);
//    }

//    @Override
//    public void readNotification(String username, Integer notificationId) {
//        Notification notification = notificationRepository.findById(notificationId).get();
//        Account account = accountRepository.findByUsername(username);
//        Collection<Account> accounts = notification.getAccount();
//        boolean isPresent = accounts.stream().filter(x -> x.getUsername().equals(username)).findFirst().isPresent();
//        if (!isPresent) {
//            accounts.add(account);
//            notification.setAccount(accounts);
//            notificationRepository.save(notification);
//        }
//    }
//
//    @Override
//    public void readNotificationByType(String username, Integer type) {
//        Account account = accountRepository.findByUsername(username);
//        List<Notification> notifications = notificationRepository.findAllByTypeAndAccountNotContains(type, account);
//        for (Notification notification: notifications) {
//            Collection<Account> accounts = notification.getAccount();
//            accounts.add(account);
//            notification.setAccount(accounts);
//        }
//        notificationRepository.saveAll(notifications);
//    }

    private Notification convertToDto(NotificationRequestDTO notificationRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Notification dto = modelMapper.map(notificationRequestDTO, Notification.class);
        return dto;
    }
}
