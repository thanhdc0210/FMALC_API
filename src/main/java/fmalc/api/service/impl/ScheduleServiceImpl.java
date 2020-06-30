package fmalc.api.service.impl;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.service.ConsignmentService;
import fmalc.api.service.ScheduleService;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    VehicleService vehicleService;

    @Override
    public Schedule getScheduleByConsignmentId(Integer id) {

        List<Vehicle> vehicles = vehicleService.findByStatus(VehicleStatusEnum.AVAILABLE.getValue());
        if(vehicles.size()>0){
            Vehicle vehicle = vehicleService.getVehicleByKmRunning(vehicles);

        }
        List<Consignment> consignments = consignmentService.getAllByStatus(0);

        return scheduleRepository.findScheduleByConsignment(id) ;
    }

}
