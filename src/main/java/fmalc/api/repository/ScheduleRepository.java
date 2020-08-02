package fmalc.api.repository;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import io.swagger.models.auth.In;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByConsignment_Id(int consignmentId);


    @Query("select  s from Schedule s where s.vehicle.id =?1")
    List<Schedule> checkVehicleInScheduled(int idVehicle);

    @Query("select  s from Schedule s where s.driver.id =?1")
    List<Schedule> checkDriverInScheduled(int idDriver);

    @Query("select  s from Schedule s where s.driver.id =?1 and s.vehicle.id =?2 and  s.consignment.id = ?3")
    Schedule findScheduleByVeDriCons(int idDriver, int idVehicle, int idConsignment);

    @Query("Select s From Schedule s" +
            " Where s.consignment.status IN :status and s.driver.account.username = :username" +
            " and s.isApprove = true")

    List<Schedule> findByConsignmentStatusAndUsername(@Param("status") List<Integer> status, @Param("username") String username);

    @Modifying
//    @Query(value = "update Schedule  s set s.vehicle.id= :idVehicle, s.driver.id= :idDriver, s.isApprove= :status where s.id=:id",nativeQuery = true)
    @Query(value = "update schedule s set s.vehicle_id=:idVehicle, s.driver_id =:idDriver, s.is_approve=:status where s.id=:id", nativeQuery = true)
    int updateStatusSchedule(@Param("idVehicle") int idVehicle, @Param("idDriver")int idDriver, @Param("status") int status,@Param("id")  int id);


    List<Schedule> findScheduleByConsignmentId(Integer consignmentId);
    List<Schedule> findByConsignmentOwnerNameContaining(String ownerName);
    List<Schedule> findByVehicleLicensePlatesContaining(String licensePlate);

    // THANHDC
//    @Query("Select count(distinct s.id) From schedule s, driver d, consignment c, place p " +
//            "where d.id = :id and p.planned_time between :startDate and :endDate " +
//            "and s.is_approve = true and s.consignment_id = c.id and s.driver_id = d.id " +
//            "and c.id = p.consignment_id and c.status != 3")
//    Integer countScheduleNumberInADayOfDriver(@Param("id") Integer id,
//                                              @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}
