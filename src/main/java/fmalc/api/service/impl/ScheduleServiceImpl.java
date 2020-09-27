
package fmalc.api.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.enums.SearchTypeForDriverEnum;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    NotificationService notificationService;

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
    public Schedule getScheduleByVehicleAndConsignment(int idCon, int idVehicle) {
        return scheduleRepository.getScheduleByVehicleAndConsignment(idCon,idVehicle);
    }

    @Override
    public List<ScheduleForConsignmentDTO> getScheduleForVehicle(int idVehicle) {
        List<Schedule> schedules = scheduleRepository.checkVehicleInScheduled(idVehicle, ConsignmentStatusEnum.COMPLETED.getValue());
        ScheduleForConsignmentDTO sc = new ScheduleForConsignmentDTO();
        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = sc.mapToListResponse(schedules);
        return scheduleForConsignmentDTOS;
    }

    @Override
    public ConsignmentResponseDTO createSchedules(MultipartFile file, String request) {
        boolean result = false;
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        JsonArray jsonArray = (JsonArray) jsonObject.get("obejctScheDTOS");
        List<ObejctScheDTO> obejctScheDTOS = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject post_id = (JsonObject) jsonArray.get(i);
            ObejctScheDTO obejctScheDTO = new ObejctScheDTO();
            post_id.get("vehicle_id").getAsInt();
            obejctScheDTO.setVehicle_id(post_id.get("vehicle_id").getAsInt());
            obejctScheDTO.setDriver_id(post_id.get("driver_id").getAsInt());
            if (post_id.get("consignment_id") != null) {
                obejctScheDTO.setConsignment_id(post_id.get("consignment_id").getAsInt());
            }
            obejctScheDTOS.add(obejctScheDTO);
        }
        ConsignmentRequestDTO consignmentRequestDTO = new Gson().fromJson(jsonObject.get("consignmentRequestDTO"), ConsignmentRequestDTO.class);
        RequestSaveScheObjDTO requestSaveScheObjDTO1 = new RequestSaveScheObjDTO();
        requestSaveScheObjDTO1.setConsignmentRequestDTO(consignmentRequestDTO);
        requestSaveScheObjDTO1.setObejctScheDTOS(obejctScheDTOS);
        List<Schedule> schedules = new ArrayList<>();
        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        if (request != null) {
            schedules = createSchedule(requestSaveScheObjDTO1, file);
            try {
                if (schedules.size() > 0) {
                    Consignment consignment = consignmentService.findById(schedules.get(0).getConsignment().getId());
                    consignmentResponseDTO = consignmentResponseDTO.mapToResponse(consignment);
                    NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
                    for (Schedule schedule : schedules) {
                        notificationRequestDTO.setVehicle_id(schedule.getVehicle().getId());
                        notificationRequestDTO.setDriver_id(schedule.getDriver().getId());
                        notificationRequestDTO.setStatus(false);
                        notificationRequestDTO.setContent("Bạn có lịch chạy mới của lô hàng #" + schedule.getConsignment().getId());
                        notificationRequestDTO.setType(NotificationTypeEnum.TASK_SCHEDULE.getValue());
                        notificationService.createNotification(notificationRequestDTO);
                    }

                    return (consignmentResponseDTO);
                }

            } catch (Exception e) {
                return null;
            }

        }
        return consignmentResponseDTO;
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
        List<Schedule> schedules = scheduleRepository.checkVehicleInScheduled(idVehicle, ConsignmentStatusEnum.COMPLETED.getValue());
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

        status.setVehicle_status(vehicleService.updateStatus(statusToUpdateDTO.getVehicle_status(), schedule.getVehicle().getId()));
        status.setDriver_status(driverService.updateStatus(statusToUpdateDTO.getDriver_status(), schedule.getDriver().getId()));
        if(statusToUpdateDTO.getConsignment_status()>=0){
            consignmentService.updateStatus(statusToUpdateDTO.getConsignment_status(), schedule.getConsignment().getId());
        }

        Consignment consignment = consignmentService.findById(schedule.getConsignment().getId());
        status.setConsignment_status(consignment.getStatus());

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

    public Integer findScheduleIdByConsignmentIdAndDriverId(Integer consignmentId, Integer driverId){
        return scheduleRepository.findScheduleIdByConsignmentIdAndDriverId(consignmentId, driverId);
    }

    @Override
    public Integer findScheduleIdByContentOfNotificationAndDriverId(String content, Integer driver_id) {

        String subString[] = content.split("#");
        String subStringId[] = subString[subString.length - 1].split("\\s");
        Integer consignmentId =  Integer.valueOf(subStringId[0]);

        return scheduleService.findScheduleIdByConsignmentIdAndDriverId(consignmentId, driver_id);
    }

    @Override
    public Integer findConsignmentFirst(int idDriver) {
        List<Consignment> consignments = scheduleRepository.findConsignmentFirst(idDriver);
//        consignments.sort(Comparator.comparing(Cons));
        if(consignments.size()>0){
            return consignments.get(0).getId();
        }
        return 0;
    }

//    @Override
//    public Schedule findScheduleByConsignment_IdAndDriver_Id(Integer consignmentId, Integer driverId) {
//
//        return scheduleRepository.findScheduleByConsignment_IdAndDriver_Id(consignmentId, driverId);
//    }

}

