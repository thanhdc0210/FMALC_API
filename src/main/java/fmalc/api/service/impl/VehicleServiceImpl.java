package fmalc.api.service.impl;

import fmalc.api.entity.Consignment;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public List<String> findVehicleLicensePlatesForReportInspection(List<Integer> status, String username, Timestamp currentDate) {
        return vehicleRepository.findVehicleLicensePlatesForReportInspection(status, username, currentDate);
    }
}
