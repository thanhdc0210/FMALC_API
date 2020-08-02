
package fmalc.api.service;

import fmalc.api.dto.*;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Driver;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.SearchTypeForDriverEnum;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;


public interface ScheduleService {
    List<ScheduleForLocationDTO> getScheduleByConsignmentId(int id);
    Schedule createSchedule(List<ObejctScheDTO> obejctScheDTOs, Consignment consignment);

    List<ScheduleForConsignmentDTO> getScheduleForVehicle(int idVehicle) ;

    List<ScheduleForLocationDTO> getScheduleToCheck();

    List<Schedule> checkVehicleInScheduled(int idVehicle);

    List<Schedule> findByConsignmentStatusAndUsername(List<Integer> status, String username);
    Schedule findById(Integer id);
    Schedule findScheduleByVehDriCon(ObejctScheDTO obejctScheDTO);
    List<Schedule> checkDriverInScheduled(int idDriver);


    StatusToUpdateDTO updateStautsForVeDriAndCon(StatusToUpdateDTO statusToUpdateDTO, Schedule schedule);

    List<Schedule> searchByTypeForDriver(String value, SearchTypeForDriverEnum searchType);

//    THANHDC
//    Integer countScheduleNumberInADayOfDriver(Integer id, Timestamp startDate, Timestamp endDate);
}
