package fmalc.api.service.impl;

import fmalc.api.dto.AlertRequestDTO;
import fmalc.api.entity.Alert;
import fmalc.api.entity.Driver;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.repository.AlertRepository;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    AlertRepository alertRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public List<Alert> getAlerts() {
        return alertRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Alert driverSendAlert(AlertRequestDTO alertRequestDTO) {
        Alert alert = new Alert();
        Driver driver = new Driver();
        Vehicle vehicle = new Vehicle();
        try{
            if (alertRequestDTO.getDriverId() > 0) {
                driver = driverRepository.findByIdAndAccountIsActive(alertRequestDTO.getDriverId(), true);
            }if(alertRequestDTO.getVehicleId()>0){
                vehicle = vehicleRepository.findByIdEqualsAndStatusIsNotLike(alertRequestDTO.getVehicleId(), VehicleStatusEnum.SOLD.getValue());

            }
        } catch (Exception e){
            System.out.println(e);
        }
        if(driver != null && vehicle != null){
            alert.setContent(alertRequestDTO.getContent());
            alert.setDriver(driver);
            alert.setVehicle(vehicle);
            alert.setTime(Timestamp.valueOf(LocalDateTime.now()));
            alert.setLevel(alertRequestDTO.getLevel());
            alert.setStatus(false);
            return  alertRepository.save(alert);
        }

        return null;
    }
}
