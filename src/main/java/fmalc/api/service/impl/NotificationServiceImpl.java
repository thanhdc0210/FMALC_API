package fmalc.api.service.impl;

import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.dto.NotificationResponeDTO;
import fmalc.api.dto.NotificationUnread;
import fmalc.api.entity.*;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.NotificationRepositry;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepositry notificationRepositry;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public Notification createNotifiation(NotificationRequestDTO dto) throws ParseException {
        Date date = new Date();
        Notification notify = convertToDto(dto);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(df.format(date));
        Timestamp timestamp = new Timestamp(date.getTime());
        notify.setTime(timestamp);

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

        notify.setDriver(driver);
        notify.setStatus(false);
        return notificationRepositry.save(notify);
    }

    @Override
    public NotificationUnread countNotificationUnread() {
        NotificationUnread result = new NotificationUnread();
        result.setCount(notificationRepositry.countAllByStatusFalse());
        result.setNotificationsUnread(new NotificationResponeDTO().mapToListResponse(notificationRepositry.findTop4ByStatusIsFalseOrderByIdDesc()));
        return result;
    }

    private Notification convertToDto(NotificationRequestDTO notificationRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Notification dto = modelMapper.map(notificationRequestDTO, Notification.class);
        return dto;
    }
}
