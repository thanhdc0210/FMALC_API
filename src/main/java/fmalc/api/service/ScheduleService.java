
package fmalc.api.service;

import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.dto.ScheduleForLocationDTO;
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
     List<Vehicle> findVehicleForSchedule(Consignment consignment) throws ParseException;
    List<Driver> findDriverForSchedule(Vehicle vehicle, Consignment consignment);
    List<ScheduleForLocationDTO> checkScheduleForVehicle(int idVehicle) ;
    List<ScheduleForLocationDTO> checkScheduleForDriver( int idDriver);
}
