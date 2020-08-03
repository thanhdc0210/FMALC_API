package fmalc.api.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.dto.NotificationResponeDTO;
import fmalc.api.dto.NotificationUnread;
import fmalc.api.entity.Driver;
import fmalc.api.entity.Notification;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.NotificationRepositry;
import fmalc.api.repository.VehicleRepository;
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
    NotificationRepositry notificationRepositry;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    private Logger logger = Logger.getLogger("MYLOG");

    @Override
    public Notification createNotification(NotificationRequestDTO dto) throws ParseException {
//        Date date = new Date();
        Notification notify = convertToDto(dto);

//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(df.format(date));

//        Timestamp timestamp = new Timestamp(date.getTime());
//        notify.setTime(timestamp);

//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(df.format(date));
//        Timestamp timestamp = new Timestamp(date.getTime());
//        notify.setTime(timestamp);
        notify.setTime(new Timestamp(System.currentTimeMillis()));


//        notify = convertToDto(dto);
        Vehicle vehicle = new Vehicle();
        vehicle = vehicleRepository.findByIdVehicle(dto.getVehicle_id());
        notify.setVehicle(vehicle);
        Driver driver = new Driver();
        try {
            driver =driverRepository.findById(dto.getDriver_id());
        }catch (Exception e){
            e.printStackTrace();
        }

//        notify.setDriver(driver);
        notify.setStatus(false);

        // Send notification to android
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

        return notificationRepositry.save(notify);
    }

    @Override
    public NotificationUnread countNotificationUnread() {
        NotificationUnread result = new NotificationUnread();
        result.setCount(notificationRepositry.countAllByStatusFalse());
        result.setNotificationsUnread(new NotificationResponeDTO().mapToListResponse(notificationRepositry.findTop4ByStatusIsFalseOrderByIdDesc()));
        return result;
    }

    @Override
    public List<Notification> getNotificationsByType(int type) {
        return notificationRepositry.findAllByTypeOrderByIdDesc(type);
    }

    public List<Notification> findByDriverId(Integer driverId) {

        return notificationRepositry.findByDriverId(driverId);
    }

    private Notification convertToDto(NotificationRequestDTO notificationRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Notification dto = modelMapper.map(notificationRequestDTO, Notification.class);
        return dto;
    }
}
