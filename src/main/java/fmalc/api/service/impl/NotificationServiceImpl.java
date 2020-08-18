package fmalc.api.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import fmalc.api.dto.DayOffRespsoneDTO;
import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.dto.NotificationResponeDTO;
import fmalc.api.dto.NotificationUnread;
import fmalc.api.entity.*;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.repository.*;
import fmalc.api.service.AccountNotificationService;
import fmalc.api.service.AccountService;
import fmalc.api.service.FleetManagerService;
import fmalc.api.service.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    AccountService accountService;

    @Autowired
    AccountNotificationRepository accountNotificationRepository;

    @Autowired
    DayOffRepository dayOffRepository;

    @Autowired
    FleetManagerService fleetManagerService;

    private static final String ADMIN ="ROLE_ADMIN";
    private static final String FLEET_MANAGER ="ROLE_FLEET_MANAGER";
    private Logger logger = Logger.getLogger("MYLOG");

    @Override
//    @Transactional
    public Notification createNotification(NotificationRequestDTO dto) throws ParseException {
//<<<<<<< HEAD
//         Notification notify = convertToDto(dto);
//        notify.setTime(new Timestamp(System.currentTimeMillis()));
//        if (dto.getType() == NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue() ||
//                dto.getType() == NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue()) {
//                notify.setVehicle(null);
//=======
        Notification notify = convertToDto(dto);
        Driver driver = driverRepository.findById(dto.getDriver_id()).get();
        notify.setTime(new Timestamp(System.currentTimeMillis()));
        if (dto.getType() == NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue() ||
                dto.getType() == NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue() ||
                    dto.getType() == NotificationTypeEnum.ALERT.getValue()) {
            DayOff dayOff = new DayOff();
            dayOff.setDriver(driver);
            dayOff.setFleetManager(driver.getFleetManager());
            dayOff.setIsApprove(dto.getType());
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
//>>>>>>> 4d2e225e21df534d779ef82ffb6c325b6f3e61b5
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
//<<<<<<< HEAD
//                 List<Account> accounts = accountRepository.findAllByIsActiveIsTrueAndRole_Role("ROLE_ADMIN");
//                Driver driver = driverRepository.findById(dto.getDriver_id()).get();
//=======
                List<Account> accounts = accountRepository.findAllByIsActiveIsTrueAndRole_Role("ROLE_ADMIN");
//>>>>>>> 4d2e225e21df534d779ef82ffb6c325b6f3e61b5
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

                if (dto.getType() != NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue() &&
                        dto.getType() != NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue() &&
                        dto.getType() != NotificationTypeEnum.ALERT.getValue()) {
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
    public List<DayOffRespsoneDTO> getNotificationsDayOff(String username) {
        Account account = new Account();

        DayOffRespsoneDTO dayOffRespsoneDTO = new DayOffRespsoneDTO();
        List<DayOffRespsoneDTO> dayOffRespsoneDTOS = new ArrayList<>();
        account = accountRepository.findByUsernameRole(username);
        List<DayOff> dayOffs = dayOffRepository.findAllByOrderByIdDesc();
        if(account.getRole().getRole().equals(ADMIN)){
            dayOffRespsoneDTOS = dayOffRespsoneDTO.mapToListResponse(dayOffs);
        }else if(account.getRole().getRole().equals(FLEET_MANAGER)){
            FleetManager fleetManager = fleetManagerService.findByAccount(account.getId());
            for(int i=0; i< dayOffs.size();i++){
                Driver driver = dayOffs.get(i).getDriver();
                if(driver.getFleetManager().getId()== fleetManager.getId()){
                    dayOffRespsoneDTO =dayOffRespsoneDTO.convertDTO(dayOffs.get(i));
//                    if(dayOffs.get(i).getIsApprove()){
//                        dayOffRespsoneDTO.setIsApprove(null);
//                    }
                    dayOffRespsoneDTOS.add(dayOffRespsoneDTO);
                }


            }
        }
        return dayOffRespsoneDTOS;
    }




    private Notification convertToDto(NotificationRequestDTO notificationRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Notification dto = modelMapper.map(notificationRequestDTO, Notification.class);
        return dto;
    }
}
