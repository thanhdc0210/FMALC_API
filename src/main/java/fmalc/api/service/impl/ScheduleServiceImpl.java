
package fmalc.api.service.impl;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.ScheduleConsginmentEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    MaintenanceService maintainanceService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PlaceService placeService;

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
    public Schedule createSchedule(List<ObejctScheDTO> obejctScheDTOs, Consignment consignment) {
        Schedule schedule= new Schedule();
        for (int i = 0; i < obejctScheDTOs.size(); i++) {
            VehicleForDetailDTO vehicleForDetailDTO = vehicleService.findVehicleById(obejctScheDTOs.get(i).getVehicle_id());
            Vehicle vehicle = vehicleForDetailDTO.convertToEnity(vehicleForDetailDTO);
            Driver driver = driverService.findById(obejctScheDTOs.get(i).getDriver_id());
            schedule = new Schedule();
            schedule.setNote("");
            schedule.setImageConsignment("");
            schedule.setConsignment(consignment);
            schedule.setDriver(driver);
            schedule.setVehicle(vehicle);
            schedule.setIsApprove(true);
            schedule.setId(null);
            schedule = scheduleRepository.save(schedule);
        }
        return schedule;
    }

    @Override
    public List<ScheduleForConsignmentDTO> getScheduleForVehicle(int idVehicle) {
        List<Schedule> schedules = scheduleRepository.checkVehicleInScheduled(idVehicle);
        ScheduleForConsignmentDTO sc = new ScheduleForConsignmentDTO();
        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = sc.mapToListResponse(schedules);
        return scheduleForConsignmentDTOS;
    }

      @Override
    public List<ScheduleForLocationDTO> getScheduleToCheck() {
        ScheduleForLocationDTO scheduleForLocationDTO = new ScheduleForLocationDTO();
        List<Schedule> schedules = scheduleRepository.findAll();

        List<ScheduleForLocationDTO> scheduleForLocationDTOS = scheduleForLocationDTO.mapToListResponse(schedules);
        for (int i = 0; i < schedules.size(); i++) {
            scheduleForLocationDTOS.get(i).setVehicle_id(schedules.get(i).getVehicle().getId());
            scheduleForLocationDTOS.get(i).setDriver_id(schedules.get(i).getDriver().getId());
        }
        return scheduleForLocationDTOS;
    }

    @Override
    public List<Schedule> checkVehicleInScheduled(int idVehicle) {
        List<Schedule> schedules = scheduleRepository.checkVehicleInScheduled(idVehicle);
        return schedules;
    }

    @Override

    public List<Schedule> findByConsignmentStatusAndUsername(List<Integer> status, String username) {


        return scheduleRepository.findByConsignmentStatusAndUsername(status, username);
    }

    @Override
    public Schedule findById(Integer id) {
        if (!scheduleRepository.existsById(id)) {
            return null;
        }
        return scheduleRepository.findById(id).get();
    }

    @Override
    public Schedule findScheduleByVehDriCon(ObejctScheDTO obejctScheDTO) {
        return scheduleRepository.
                findScheduleByVeDriCons(obejctScheDTO.getDriver_id(), obejctScheDTO.getVehicle_id(), obejctScheDTO.getConsignment_id());
    }

    @Override
    public List<Schedule> checkDriverInScheduled(int idDriver) {
        List<Schedule> schedules = scheduleRepository.checkDriverInScheduled(idDriver);
        return schedules;
    }


}

