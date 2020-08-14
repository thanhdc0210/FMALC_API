
package fmalc.api.service.impl;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.SearchTypeForDriverEnum;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    UploaderService uploaderService;

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
    public List<Schedule> createSchedule(RequestSaveScheObjDTO requestSaveScheObjDTO, MultipartFile file) {
        Consignment consignment = new Consignment();
        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        List<ObejctScheDTO> obejctScheDTOS = requestSaveScheObjDTO.getObejctScheDTOS();
        List<Schedule> schedules = new ArrayList<>();
        ConsignmentRequestDTO consignmentRequestDTO = requestSaveScheObjDTO.getConsignmentRequestDTO();
        try {
            if(!file.isEmpty()){
                String link = uploaderService.upload(file);
                consignmentRequestDTO.setImageConsignment(link);
            }

            consignment = consignmentService.save(consignmentRequestDTO);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        Schedule schedule = new Schedule();

        if (consignment.getId() != null) {


            for (int i = 0; i < obejctScheDTOS.size(); i++) {
                VehicleForDetailDTO vehicleForDetailDTO = vehicleService.findVehicleById(obejctScheDTOS.get(i).getVehicle_id());
                Vehicle vehicle = vehicleForDetailDTO.convertToEnity(vehicleForDetailDTO);
                Driver driver = driverService.findById(obejctScheDTOS.get(i).getDriver_id());
                schedule = new Schedule();

                schedule.setImageConsignment("");
                schedule.setConsignment(consignment);
                schedule.setDriver(driver);
                schedule.setVehicle(vehicle);
                schedule.setIsApprove(true);
                schedule.setId(null);
                schedule = scheduleRepository.save(schedule);
                if(obejctScheDTOS.get(i).getConsignment_id()>0){
                    driver =driverService.findById(obejctScheDTOS.get(i).getConsignment_id());
                   Schedule tmp = new Schedule();

                    tmp.setImageConsignment("");
                    tmp.setConsignment(consignment);
                    tmp.setDriver(driver);
                    tmp.setVehicle(vehicle);
                    tmp.setIsApprove(true);
                    tmp.setId(null);
                    tmp.setInheritance(schedule);
                    tmp = scheduleRepository.save(tmp);
                    schedules.add(tmp);
                }

                schedules.add(schedule);
            }

        }

        return schedules;
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

    // Get the list of schedules of the driver
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

    @Override
    public int checkConsignmentStatus(int idDriver, int status, int statusDe) {
        return scheduleRepository.checkConsignmentStatus(idDriver, status, statusDe).size();
    }

    @Override
    public Schedule getScheduleRunningForDriver(int idDriver) {
        return scheduleRepository.findConsignmentRuning(idDriver, ConsignmentStatusEnum.OBTAINING.getValue(),ConsignmentStatusEnum.DELIVERING.getValue());
    }

    @Override
    public StatusToUpdateDTO updateStautsForVeDriAndCon(StatusToUpdateDTO statusToUpdateDTO, Schedule schedule) {
        StatusToUpdateDTO status = new StatusToUpdateDTO();
//        Consignment consignment = consignmentService.findById(schedule.getConsignment().getId());
//        if (consignment.getStatus() == ConsignmentStatusEnum.WAITING.getValue()) {
        status.setVehicle_status(vehicleService.updateStatus(statusToUpdateDTO.getVehicle_status(), schedule.getVehicle().getId()));
        status.setDriver_status(driverService.updateStatus(statusToUpdateDTO.getDriver_status(), schedule.getDriver().getId()));
        status.setConsignment_status(consignmentService.updateStatus(statusToUpdateDTO.getConsignment_status(), schedule.getConsignment().getId()));

//        }
        return status;
    }

    @Override
    public List<Schedule> searchByTypeForDriver(String value, SearchTypeForDriverEnum searchType, Integer driverId) {
        List<Schedule> result = new ArrayList<>();
        switch (searchType) {
            case CONSIGNMENT_ID:
                Integer parsed = Integer.parseInt(value);
                result = scheduleRepository.findScheduleByConsignmentIdAndDriverIdAndIsApprove(parsed, driverId,true);
                return result;
            case LICENSE_PLATE:
                result = scheduleRepository.findByVehicleLicensePlatesContainingAndDriverIdAndIsApprove(value, driverId,true);
                return result;
            case OWNER_NAME:
                result = scheduleRepository.findByConsignmentOwnerNameContainingAndDriverIdAndIsApprove(value, driverId,true);
                return result;
            default:
                throw new IllegalStateException("Unexpected value: " + searchType);
        }

    }

    @Override
    public Schedule getScheduleByDriverSub(int id) {
        return scheduleRepository.findScheduleBySchedule(id);
    }

    // Start date : lúc bấm nút kết thúc   --  THANHDC
    @Override
    public Integer countScheduleNumberInADayOfDriver(Integer id, Timestamp startDate, Timestamp endDate) {

        return scheduleRepository.countScheduleNumberInADayOfDriver(id, startDate, endDate);
    }

    // THANHDC

//    @Override
//    public Schedule findScheduleByConsignment_IdAndDriver_Id(Integer consignmentId, Integer driverId) {
//
//        return scheduleRepository.findScheduleByConsignment_IdAndDriver_Id(consignmentId, driverId);
//    }

}

