package fmalc.api.service.impl;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.DriverStatusEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.schedule.ScheduleForConsignment;
import fmalc.api.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    MaintainanceService maintainanceService;

    @Autowired
    DeliveryDetailService deliveryDetailService;

    private static int priorityPlace = 1;

    @Override
    public List<ScheduleForLocationDTO> getScheduleByConsignmentId(int id) {
        //-----------------------------------------------------------------------
        ScheduleForLocationDTO scheduleForLocationDTO = new ScheduleForLocationDTO();
        List<Schedule> schedules = scheduleRepository.findByConsignment_Id(id);
        List<ScheduleForLocationDTO> scheduleForLocationDTOs = new ArrayList<>();
        for (int i = 0; i < schedules.size(); i++) {
           scheduleForLocationDTO = scheduleForLocationDTO.convertSchedule(schedules.get(i));
            scheduleForLocationDTO.setVehicle_id(schedules.get(i).getVehicle().getId());
            scheduleForLocationDTO.setDriver_id(schedules.get(i).getDriver().getId());
            scheduleForLocationDTOs.add(scheduleForLocationDTO);
        }
        return scheduleForLocationDTOs;
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {

        return scheduleRepository.save(schedule);


    }


    //find vehicle with status = available
    @Override
    public Vehicle findVehicleForSchedule(Consignment consignment) {
        ScheduleForConsignment scheduleForConsignment = new ScheduleForConsignment();
        boolean flag = true;
        List<Vehicle> vehiclesAvailable = vehicleService.findByStatus(VehicleStatusEnum.AVAILABLE.getValue(), consignment.getWeight());
        List<Vehicle> vehiclesScheduled = vehicleService.findByStatus(VehicleStatusEnum.SCHEDULED.getValue(), consignment.getWeight());
        Vehicle vehicle = new Vehicle();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if (vehiclesAvailable.size() > 0) {
            vehiclesAvailable= scheduleForConsignment.checkMaintainForVehicle(vehiclesAvailable,consignment);
            vehiclesAvailable = scheduleForConsignment.checkScheduledForVehicle(vehiclesScheduled,consignment);
            if (vehiclesAvailable.size() > 0) {
                vehicle = vehicleService.getVehicleByKmRunning(vehiclesAvailable);
            }
        }
        return vehicle;
    }


    //find vehicle with status = schedule
    @Override
    public  List<ScheduleForLocationDTO> checkScheduleForVehicle(int idVehicle) {
        List<ScheduleForLocationDTO> scheduleForLocationDTOS = new ArrayList<>();
        ScheduleForLocationDTO scheduleForLocationDTO = new ScheduleForLocationDTO();
        List<Integer> id = new ArrayList<>();
        boolean flag;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
//        date = sdf.parse(sdf.format(date));
//        java.sql.Date date1 = new java.sql.Date(date.getTime());
        List<Schedule> Schedules = scheduleRepository.checkVehicleInScheduled(idVehicle);
        if(Schedules.size()>0){

            scheduleForLocationDTOS = scheduleForLocationDTO.mapToListResponse(Schedules);
            if(scheduleForLocationDTOS.size()>0){
                for(int i=0; i< scheduleForLocationDTOS.size();i++){
                    Place deliveryDetail =
                            deliveryDetailService.getDeliveryByConsignmentAndPriority(scheduleForLocationDTOS.get(i).getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
                    PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
                    placeResponeDTO = placeResponeDTO.convertPlace(deliveryDetail);
                    String datePlace = sdf.format(placeResponeDTO.getPlannedTime());
                    String dateNow = sdf.format(new Date());
                    if(dateNow.compareTo(datePlace)<=0){
                        id.add(scheduleForLocationDTOS.get(i).getId());
                    }
                }
                for (int j = 0; j < id.size(); j++) {
                    flag = true;
                    for (int i = 0; i < scheduleForLocationDTOS.size(); i++) {
                        if(flag){
                            if (scheduleForLocationDTOS.get(i).getId() == id.get(j)) {
                                scheduleForLocationDTOS.remove(scheduleForLocationDTOS.get(i));
                                flag = false;
                            }
                        }
                    }

                }

            }


        }
        return scheduleForLocationDTOS;
    }

    @Override
    public  List<ScheduleForLocationDTO> checkMaintainForDriver(int idDriver) {
        return null;
    }




}
