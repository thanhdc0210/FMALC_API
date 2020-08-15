package fmalc.api.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import fmalc.api.dto.DayOffNotificationResponseDTO;
import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.dto.NotificationResponeDTO;
import fmalc.api.dto.NotificationUnread;
import fmalc.api.entity.*;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.repository.*;
import fmalc.api.service.AccountNotificationService;
import fmalc.api.service.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    AccountNotificationService accountNotificationService;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountNotificationRepository accountNotificationRepository;

    @Autowired
    DayOffRepository dayOffRepository;

    private Logger logger = Logger.getLogger("MYLOG");

    @Override
//    @Transactional
    public Notification createNotification(NotificationRequestDTO dto) throws ParseException {
        Notification notify = convertToDto(dto);
        Driver driver = driverRepository.findById(dto.getDriver_id()).get();
        notify.setTime(new Timestamp(System.currentTimeMillis()));
        if (dto.getType() == NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue() ||
                dto.getType() == NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue() ||
                    dto.getType() == NotificationTypeEnum.ALERT.getValue()) {
            DayOff dayOff = new DayOff();
            dayOff.setDriver(driver);
            dayOff.setFleetManager(driver.getFleetManager());
            dayOff.setIsApprove(false);
            if (dto.getType() == NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue()) {
                dayOff.setNote(dto.getContent());
                String[] dateString = dto.getContent().split("\\|");
                java.util.Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateString[0]);
                java.util.Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateString[1]);
                dayOff.setStartDate(new java.sql.Date(startDate.getTime()));
                dayOff.setEndDate(new java.sql.Date(endDate.getTime()));
            } else if(dto.getType() == NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue()){
                dayOff.setNote(dto.getContent());
                Date today = new Date(Calendar.getInstance().getTime().getTime());
                dayOff.setStartDate(today);
                dayOff.setEndDate(today);
            }
            try{
            dayOffRepository.save(dayOff);

            } catch (Exception e){
                e.printStackTrace();
            }
            notify.setVehicle(null);
        } else {
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicle_id()).get();
            notify.setVehicle(vehicle);
        }
        notify.setType(dto.getType());
        notify.setContent(dto.getContent());
        notify.setDriver(driver);

        try {
            Notification notification = notificationRepository.save(notify);

            if (notification != null) {
                List<Account> accounts = accountRepository.findAllByIsActiveIsTrueAndRole_Role("ROLE_ADMIN");
                Driver driver = driverRepository.findById(dto.getDriver_id()).get();
                accounts.add(accountRepository.findById(driver.getFleetManager().getAccount().getId()).get());
                List<AccountNotification> accountNotifications = new ArrayList<>();
                for (Account acc: accounts) {
                    AccountNotificationKey accountNotificationKey = new AccountNotificationKey(acc.getId(), notification.getId());
                    accountNotifications.add(AccountNotification.builder()
                            .id(accountNotificationKey)
                            .account(acc)
                            .notification(notification)
                            .status(false)
                            .build());
                }
                accountNotificationRepository.saveAll(accountNotifications);

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
                            dto.getType() != NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue() &&
                                dto.getType() != NotificationTypeEnum.ALERT.getValue()) {
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
    public List<DayOffNotificationResponseDTO> getNotificationsDayOff() {
        List<Notification> notifications =notificationRepository.findAllByTypeInOrderByIdDesc(Arrays.asList(4, 5));
        List<DayOffNotificationResponseDTO> dayOffNotificationResponseDTOS = new DayOffNotificationResponseDTO().mapToListResponse(notifications);
        DayOffNotificationResponseDTO dayOffDTO = new DayOffNotificationResponseDTO();
        AccountNotification an = new AccountNotification();
        Account account = new Account();
        for(int i=0; i< dayOffNotificationResponseDTOS.size();i++){

            dayOffDTO = dayOffNotificationResponseDTOS.get(i);

            account = accountRepository.findByDriverId(dayOffDTO.getDriver().getId());
            an =  accountNotificationService.findByFleetAndNoti(account.getId(),notifications.get(i).getId());
            DayOff dayOff = dayOffRepository.getDayOffExistByDateIsApprove(dayOffDTO.getDriver().getId(),new Date(dayOffDTO.getStartDate().getTime()), new Date(dayOffDTO.getEndDate().getTime()));
            dayOffNotificationResponseDTOS.get(i).setIsRead(an.getStatus());
            if(dayOff!=null){
                dayOffNotificationResponseDTOS.get(i).setIsApprove(dayOff.getIsApprove());
            }else{
                dayOffNotificationResponseDTOS.get(i).setIsApprove(false);
            }
        }

        return dayOffNotificationResponseDTOS;
    }

    private Notification convertToDto(NotificationRequestDTO notificationRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Notification dto = modelMapper.map(notificationRequestDTO, Notification.class);
        return dto;
    }
}
