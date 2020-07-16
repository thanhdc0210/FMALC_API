
package fmalc.api.service;

import fmalc.api.dto.*;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Driver;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;


public interface ScheduleService {
    List<ScheduleForLocationDTO> getScheduleByConsignmentId(int id);
    Schedule createSchedule(Schedule schedule);

     List<Vehicle> findVehicleForSchedule(Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO, int schedule) throws ParseException;
    List<Driver> findDriverForSchedule(double weight, Consignment consignment);

    List<ScheduleForConsignmentDTO> checkScheduleForVehicle(int idVehicle) ;
    List<ScheduleForConsignmentDTO> checkScheduleForDriver( int idDriver);

    List<ScheduleForConsignmentDTO> getScheduleForVehicle(int idVehicle) ;

    boolean updateStatusSchedule(ObejctScheDTO requestObjectDTO);

    List<ScheduleForLocationDTO> getScheduleToCheck();

    List<Schedule> findByConsignmentStatusAndUsername(List<Integer> status, String username);
    Schedule findById(Integer id);
    Schedule findScheduleByVehDriCon(ObejctScheDTO obejctScheDTO);
    List<ScheduleForConsignmentDTO> findScheduleForFuture(List<Vehicle> vehicles, Consignment consignment,ConsignmentRequestDTO consignmentRequestDTO);

}
