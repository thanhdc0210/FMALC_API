
package fmalc.api.service;

import fmalc.api.dto.*;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Schedule;
import fmalc.api.enums.SearchTypeForDriverEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.sql.Timestamp;
import java.util.List;


public interface ScheduleService {
    ScheduleToConfirmDTO scheduleConfirm (RequestObjectDTO requestObjectDTO, int driver_sub);
    ScheduleToConfirmDTO scheduleReturn(Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO, List<ScheduleForConsignmentDTO> scheduleds, int driver_sub);
    List<ScheduleForLocationDTO> getScheduleByConsignmentId(int id);
    List<Schedule> createSchedule(RequestSaveScheObjDTO requestSaveScheObjDTO , MultipartFile file);
    Schedule getScheduleByVehicleAndConsignment(int idCon, int idVehicle);
    List<ScheduleForConsignmentDTO> getScheduleForVehicle(int idVehicle) ;

    ConsignmentResponseDTO createSchedules(MultipartFile file, String request);

    List<ScheduleForLocationDTO> getScheduleToCheck();

    List<Schedule> checkVehicleInScheduled(int idVehicle);

    // Get the list of schedules of the driver
    List<Schedule> findByConsignmentStatusAndUsername(List<Integer> status, String username);

    Schedule findById(Integer id);

    Schedule findScheduleByVehDriCon(ObejctScheDTO obejctScheDTO);

    List<Schedule> checkDriverInScheduled(int idDriver);

    int  checkConsignmentStatus(int idDriver, int status, int statusDe);

    Schedule getScheduleRunningForDriver(int idDriver);

    StatusToUpdateDTO updateStautsForVeDriAndCon(StatusToUpdateDTO statusToUpdateDTO, Schedule schedule);

    List<Schedule> searchByTypeForDriver(String value, SearchTypeForDriverEnum searchType, Integer driverId);

    Schedule getScheduleByDriverSub(int id);

//    THANHDC
    Integer countScheduleNumberInADayOfDriver(Integer id, Timestamp startDate, Timestamp endDate);

    Integer findScheduleIdByConsignmentIdAndDriverId(Integer consignmentId, Integer driverId);

    Integer findScheduleIdByContentOfNotificationAndDriverId(String content, Integer driver_id);

    Integer findConsignmentFirst(int idDriver);
//    Schedule findScheduleByConsignment_IdAndDriver_Id(Integer consignmentId, Integer driverId);
}
