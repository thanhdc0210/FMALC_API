package fmalc.api.repository;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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



    @Query("Select s From Schedule s" +
            " Where s.consignment.status IN :status and s.driver.account.username = :username" +
            " and s.isApprove = true")
    List<Schedule> findByConsignmentStatusAndUsernameForDriver(@Param("status") List<Integer> status, @Param("username") String username);

}
