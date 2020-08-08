package fmalc.api.service.impl;

import fmalc.api.repository.ConsignmentRepository;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;


    @Override
    public HashMap<String, Integer> getOverviewReport() {
        HashMap<String,Integer> result = new HashMap<>();
        result.put("CONSIGNMENT",consignmentRepository.findAll().size());
        result.put("DRIVER",driverRepository.findAll().size());
        result.put("VEHICLE",vehicleRepository.findAll().size());
        return result;
    }
}
