package fmalc.api.repository;

import fmalc.api.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByConsignment_Id(int consignmentId);

    @Query("select s from Schedule s where s.consignment.id=?1")
    List<Schedule> findScheudleByConsignment(int consignmentId);

    Schedule findScheduleById(int id);

    @Query("select  s from Schedule s where s.vehicle.id =?1")
    List<Schedule> checkVehicleInScheduled(int idVehicle);

    @Query("select  s from Schedule s where s.driver.id =?1")
    List<Schedule> checkDriverInScheduled(int idDriver);

    @Query("select  s from  Schedule  s where s.inheritance.id =?1")
    Schedule findScheduleBySchedule(int id);

    @Query("select  s from Schedule s where s.driver.id =?1 and s.consignment.status = ?2 or s.driver.id =?1 and s.consignment.status = ?3")
    List<Schedule> checkConsignmentStatus(int idDriver, int status , int statusDeli);

    @Query("select  s from Schedule s where s.driver.id =?1 and s.vehicle.id =?2 and  s.consignment.id = ?3")
    Schedule findScheduleByVeDriCons(int idDriver, int idVehicle, int idConsignment);

    // Get the list of schedules of the driver
    @Query("Select s From Schedule s" +
            " Where s.consignment.status IN :status and s.driver.account.username = :username" +
            " and s.isApprove = true")
    List<Schedule> findByConsignmentStatusAndUsername(@Param("status") List<Integer> status, @Param("username") String username);

    @Modifying
//    @Query(value = "update Schedule  s set s.vehicle.id= :idVehicle, s.driver.id= :idDriver, s.isApprove= :status where s.id=:id",nativeQuery = true)
    @Query(value = "update schedule s set s.vehicle_id=:idVehicle, s.driver_id =:idDriver, s.is_approve=:status where s.id=:id", nativeQuery = true)
    int updateStatusSchedule(@Param("idVehicle") int idVehicle, @Param("idDriver") int idDriver, @Param("status") int status, @Param("id") int id);


    List<Schedule> findScheduleByConsignmentIdAndDriverIdAndIsApprove(Integer consignmentId, Integer driverId, boolean isApprove);
    List<Schedule> findByConsignmentOwnerNameContainingAndDriverIdAndIsApprove(String ownerName, Integer driverId, boolean isApprove);
    List<Schedule> findByVehicleLicensePlatesContainingAndDriverIdAndIsApprove(String licensePlate, Integer driverId, boolean isApprove);
    List<Schedule> findScheduleByConsignmentStatus(Integer consignmentStatus);

    @Query("SELECT s FROM Schedule s where s.driver.id =?1 and s.consignment.status between  ?2 and ?3")
    Schedule findConsignmentRuning(int idDriver, int start, int end);

    // THANHDC
    @Query("Select count(distinct s.id) From Schedule s, Driver d, Consignment c, Place p " +
            "where d.id = :id and p.plannedTime between :startDate and :endDate " +
            "and s.isApprove = true and s.consignment.id = c.id and s.driver.id = d.id " +
            "and c.id = p.consignment.id and c.status = 0")
    Integer countScheduleNumberInADayOfDriver(@Param("id") Integer id,
                                              @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

//    @Query("Select s from Schedule s Where s.consignment.id = :consignmentId" +
//            " And s.driver.id = :driverId")
//    Schedule findScheduleByConsignment_IdAndDriver_Id(@Param("consignmentId") Integer consignmentId, @Param("driverId") Integer driverId);

    @Query("Select s.id From Schedule s Where s.consignment.id = :consignmentId AND s.driver.id = :driverId")
    Integer findScheduleIdByConsignmentIdAndDriverId(@Param("consignmentId") Integer consignmentId, @Param("driverId") Integer driverId);
}
