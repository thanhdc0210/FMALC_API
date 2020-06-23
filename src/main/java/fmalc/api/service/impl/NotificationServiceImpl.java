package fmalc.api.service.impl;


import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.dto.VehicleTypeDTO;
import fmalc.api.entity.*;
import fmalc.api.repository.NotificationRepositry;
import fmalc.api.repository.NotificationTypeRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.DriverService;
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
    DriverService driverService;

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public Notify createNotifiation(NotificationRequestDTO dto) throws ParseException {
        Date date = new Date();
        Notify notify = convertToDto(dto);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(df.format(date));
        Timestamp timestamp = new Timestamp(date.getTime());
        notify.setTime(timestamp);


//        notify = convertToDto(dto);
        Vehicle vehicle = new Vehicle();
        vehicle = vehicleRepository.findByIdVehicle(dto.getVehicle_id());
        notify.setVehicle(vehicle);
//        NotifyType notifyType = new NotifyType();
//        if(notificationTypeRepository.findAll().isEmpty()){
//            notifyType.setNotifyTypeName("Cảnh báo xe dừng đổ");
//            notifyType = notificationTypeRepository.save(notifyType);
//
//        }else{
//            notifyType = notificationTypeRepository.findById(dto.getNotify_type_id());
//        }
        Driver driver = new Driver();
        driver =driverService.findById(dto.getDriver_id());
        notify.setDriver(driver);





        return notificationRepositry.save(notify);
    }

    private Notify convertToDto(NotificationRequestDTO notificationRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Notify dto = modelMapper.map(notificationRequestDTO, Notify.class);

        return dto;
    }
}
