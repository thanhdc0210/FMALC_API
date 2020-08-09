package fmalc.api.repository;



import fmalc.api.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

        @Query("SELECT v from Vehicle v where v.isActive =?1 ")
        List<Vehicle> getListVehicle(Boolean isActive);

        @Query("SELECT v FROM Vehicle v WHERE v.id = ?1")
        Vehicle findByIdVehicle(int id);
        @Query("SELECT v FROM Vehicle v WHERE v.licensePlates = ?1")
        Vehicle findByLicensePlates(String license);

        @Query("SELECT v FROM Vehicle v WHERE v.status = ?1 and v.weight >= ?2")
        List<Vehicle> findByStatus(int status, double weight);

        @Query("SELECT v FROM Vehicle v where  v.weight >= ?1 and v.isActive=?2")
        List<Vehicle> findByWeight( double weight,Boolean isActive);

        @Query("SELECT v FROM Vehicle v where  v.weight > ?1")
        List<Vehicle> findByWeightBigger( double weight);

        @Query("SELECT v FROM Vehicle v where  v.weight < ?1")
        List<Vehicle> findByWeightSmaller( double weight);

        @Modifying(clearAutomatically = true)
        @Transactional
        @Query(value = "UPDATE vehicle v set v.status = ?1 where v.id = ?2", nativeQuery = true)
        int updateStatusVehicle(int status, int id);


//        @Modifying(clearAutomatically = true)
//        @Transactional
//        @Query(value = "UPDATE vehicle v set v.status = ?1 where v.id = ?2", nativeQuery = true)
//        int updateKVehicle(int status, int id);


        @Query(value = "SELECT v.license_plates " +
                "FROM vehicle v " +
                "WHERE " +
                "v.id IN ( " +
                "SELECT s.vehicle_id " +
                "FROM schedule s " +
                "WHERE s.consignment_id IN ( " +
                "SELECT c.id " +
                "FROM place p , consignment c " +
                "WHERE c.id = p.consignment_id " +
                "AND timediff(now(), p.planned_time) < 0 " +
                "AND p.planned_time between now() and :endDate " +
                "AND c.status = 0 " +
                "AND p.priority = 1 " +
                "GROUP BY c.id " +
                ") " +
                "AND s.driver_id IN ( " +
                "SELECT d.id " +
                "FROM driver d " +
                "WHERE d.account_id IN ( " +
                "SELECT a.id " +
                "FROM account a " +
                "WHERE a.username = :username)) " +
                "and s.is_approve = true ) " +
                "limit 1", nativeQuery = true)
        String findVehicleLicensePlatesForReportInspectionBeforeDelivery(@Param("username") String username,
                                                                         @Param("endDate") Timestamp endDate);

        @Query(value = "Select distinct v.license_plates\n" +
                "from vehicle v, consignment c, driver d, account a, schedule s\n" +
                "where v.id = s.vehicle_id and c.id = s.consignment_id and s.driver_id = d.id\n" +
                "and d.account_id = a.id and a.username= :username\n" +
                "and c.id IN (\n" +
                "select MAX(c.id) \n" +
                "from consignment c, driver d, schedule s, account a, place p\n" +
                "where c.id = s.consignment_id and s.driver_id = d.id\n" +
                "and p.consignment_id = c.id\n" +
                "and d.account_id = a.id\n" +
                "and timediff(p.actual_time,timestamp(now())) < 0\n" +
                "and c.status = 3\n" +
                "and p.type = 1\n" +
                "and s.is_approve = true\n" +
                "and a.username = :username\n" +
                "and p.actual_time between :startDate and now() "+
                ")", nativeQuery = true)
        String findVehicleLicensePlatesForReportInspectionAfterDelivery(@Param("username") String username,
                                                                        @Param("startDate") Timestamp startDate);

        // Get vehicle --> get report_issue information của xe sắp chạy
        @Query(value = "SELECT v " +
                "FROM Vehicle v " +
                "INNER JOIN Schedule s ON v.id = s.vehicle.id " +
                "INNER JOIN Consignment c ON c.id = s.consignment.id " +
                "INNER JOIN Place p ON p.consignment.id = c.id " +
                "INNER JOIN Driver d ON d.id = s.driver.id " +
                "INNER JOIN Account a ON a.id = d.account.id " +
                "AND a.username = :username AND s.isApprove = true AND c.status IN :status " +
                "AND timediff(now(), p.plannedTime) < 0 "+
                "AND p.plannedTime between :startDate and :endDate "+
                "GROUP BY v.id "
                )
        Vehicle findVehicleByUsernameAndConsignmentStatus(@Param("username") String username,@Param("status") List<Integer> status,
                          @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

        @Modifying
        @org.springframework.transaction.annotation.Transactional
        @Query(value = "Update vehicle v set v.kilometer_running =:kmRunning where v.id =:id", nativeQuery = true)
        void updateKmRunning(@Param("id") int id, @Param("kmRunning") int kmRunning);

        Vehicle findByIdEqualsAndStatusIsNotLike(Integer id, Integer status);
        List<Vehicle> findByDateCreateBefore(Date dateBefore);

}
