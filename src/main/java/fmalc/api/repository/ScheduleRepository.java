package fmalc.api.repository;

import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByConsignment_Id(int consignmentId);


    @Query("select  s from Schedule s where s.vehicle.id =?1")
    List<Schedule> checkVehicleInScheduled(int idVehicle);
    @Query("select  s from Schedule s where s.driver.id =?1")
    List<Schedule> checkDriverInScheduled(int idDriver);

//    @Query("select s from Schedule s where s.consignment")
//    List<Schedule> getAll();

}
