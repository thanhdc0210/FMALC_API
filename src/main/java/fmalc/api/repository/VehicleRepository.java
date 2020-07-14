package fmalc.api.repository;



import fmalc.api.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

        @Query("SELECT v FROM Vehicle v WHERE v.id = ?1")
        Vehicle findByIdVehicle(int id);

        @Query("SELECT v FROM Vehicle v WHERE v.licensePlates = ?1")
        Vehicle findByLicensePlates(String license);

        @Query("SELECT v FROM Vehicle v WHERE v.status = ?1 and v.weight >= ?2")
        List<Vehicle> findByStatus(int status, double weight);

        @Query("SELECT v FROM Vehicle v where  v.weight >= ?1")
        List<Vehicle> findByWeight( double weight);

        @Modifying(clearAutomatically = true)
        @Transactional
        @Query(value = "UPDATE Vehicle v set v.status = ?1 where v.id = ?2", nativeQuery = true)
        void updateStatusVehicle(int status, int id);

        @Query(value = "SELECT v.license_plates " +
        "                FROM Vehicle v " +
        "                WHERE " +
        "                v.id IN ( " +
        "                SELECT s.vehicle_id " +
        "                FROM Schedule s " +
        "                WHERE s.consignment_id IN ( " +
        "                SELECT c.id " +
        "                FROM Place p , Consignment c " +
        "                WHERE c.id and p.consignment_id " +
        "                AND c.status IN :status " +
        "                GROUP BY c.id " +
        "                HAVING MIN(p.planned_time) BETWEEN :startDate AND :current ) " +
        "                AND s.driver_id IN ( " +
        "                SELECT d.id " +
        "                FROM Driver d " +
        "                WHERE d.account_id IN ( " +
        "                SELECT a.id " +
        "                FROM Account a " +
        "                WHERE a.username = :username)) " +
        "                and s.is_approve = true  )", nativeQuery = true)
        List<String> findVehicleLicensePlatesForReportInspectionBeforeDelivery(
                @Param("status") List<Integer> status, @Param("username") String username,
                @Param("startDate") Timestamp startDate, @Param("current") Timestamp current);

        @Query(value = "SELECT v.license_plates " +
                "                FROM Vehicle v " +
                "                WHERE " +
                "                v.id IN ( " +
                "                SELECT s.vehicle_id " +
                "                FROM Schedule s " +
                "                WHERE s.consignment_id IN ( " +
                "                SELECT c.id " +
                "                FROM Place p , Consignment c " +
                "                WHERE c.id and p.consignment_id " +
                "                AND c.status IN :status " +
                "                GROUP BY c.id " +
                "                HAVING MIN(p.actual_time) BETWEEN :startDate AND :current ) " +
                "                AND s.driver_id IN ( " +
                "                SELECT d.id " +
                "                FROM Driver d " +
                "                WHERE d.account_id IN ( " +
                "                SELECT a.id " +
                "                FROM Account a " +
                "                WHERE a.username = :username)) " +
                "                and s.is_approve = true  )", nativeQuery = true)
        List<String> findVehicleLicensePlatesForReportInspectionAfterDelivery(
                @Param("status") List<Integer> status, @Param("username") String username,
                @Param("startDate") Timestamp startDate, @Param("current") Timestamp current);

        @Query(value = "SELECT v " +
                "FROM Vehicle v " +
                "INNER JOIN Schedule s ON v.id = s.vehicle.id " +
                "INNER JOIN Consignment c ON c.id = s.consignment.id " +
                "INNER JOIN Place p ON p.consignment.id = c.id " +
                "INNER JOIN Driver d ON d.id = s.driver.id " +
                "INNER JOIN Account a ON a.id = d.account.id " +
                "AND a.username = :username AND s.isApprove = true AND c.status = 0 " +
                "GROUP BY v.id " +
                "HAVING MIN(p.plannedTime) BETWEEN :startDate AND :current")
        Vehicle findVehicleByUsernameAndTime(@Param("username") String username,
                                             @Param("startDate") Timestamp startDate, @Param("current") Timestamp current);
}
