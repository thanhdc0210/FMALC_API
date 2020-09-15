package fmalc.api.service.impl;

import fmalc.api.dto.AlertRequestDTO;
import fmalc.api.dto.AlertResponseDTO;
import fmalc.api.dto.MaintainanceResponse;
import fmalc.api.dto.Paging;
import fmalc.api.entity.Account;
import fmalc.api.entity.Alert;
import fmalc.api.entity.Driver;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.repository.AccountRepository;
import fmalc.api.repository.AlertRepository;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.AccountService;
import fmalc.api.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    AlertRepository alertRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    AccountRepository accountRepository;
    private static final String ADMIN ="ROLE_ADMIN";
    private static final String FLEET_MANAGER ="ROLE_FLEET_MANAGER";
    @Override
    public Paging getAlerts(String username,int pageCurrent) {
        Account account = accountRepository.findByUsername(username);
        Paging paging = new Paging();
        Pageable pageable = PageRequest.of(pageCurrent, paging.getNumberElements(), Sort.by("level").descending().and(Sort.by("id").descending()));
        if(account.getRole().getRole().equals(ADMIN)){
//            return alertRepository.findAllByOrderByIdDesc(pageable);
            Page page = alertRepository.findAllByOrderByIdDesc(pageable);
            paging.setList(new AlertResponseDTO().mapToListResponse(page.getContent()));
            paging.setTotalPage(page.getTotalPages());
            paging.setPageCurrent(pageCurrent);
        }else if(account.getRole().getRole().equals(FLEET_MANAGER)){
//            return  alertRepository.findAlertByDriver(username);
            Page page =alertRepository.findAlertByDriver(username,pageable);
            paging.setList(new AlertResponseDTO().mapToListResponse(page.getContent()));
            paging.setTotalPage(page.getTotalPages());
            paging.setPageCurrent(pageCurrent);
        }
        return paging;
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
                vehicle = vehicleRepository.findByIdEqualsAndStatusIsNotLike(alertRequestDTO.getVehicleId(), VehicleStatusEnum.UNAVAILABLE.getValue());

            }
        } catch (Exception e){

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

    @Override
    public Alert updateStatus(int id) {
        Alert alert = new Alert();
        if(alertRepository.updateStatus(id,true)>0){
            alert = alertRepository.findById(id).get();
        }
        return alert;
    }
}
