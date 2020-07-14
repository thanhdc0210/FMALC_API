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

        @Query("SELECT v FROM Vehicle v where  v.weight = ?1")
        List<Vehicle> findByWeight( double weight);

        @Query("SELECT v FROM Vehicle v where  v.weight > ?1")
        List<Vehicle> findByWeightBigger( double weight);

        @Query("SELECT v FROM Vehicle v where  v.weight < ?1")
        List<Vehicle> findByWeightSmaller( double weight);

        @Modifying(clearAutomatically = true)
        @Transactional
        @Query(value = "UPDATE Vehicle v set v.status = ?1 where v.id = ?2", nativeQuery = true)
        void updateStatusVehicle(int status, int id);

        @Query("SELECT v.licensePlates  " +
                "FROM Vehicle v " +
                "INNER JOIN Schedule s ON v.id = s.vehicle.id " +
                "INNER JOIN Consignment c ON c.id = s.consignment.id " +
                "INNER JOIN Place p ON p.consignment.id = c.id " +
                "INNER JOIN Driver d ON d.id = s.driver.id " +
                "INNER JOIN Account a ON a.id = d.account.id " +
                "AND a.username = :username AND s.isApprove = true AND c.status IN :status " +
                "GROUP BY v.id " +
                "HAVING MIN(p.plannedTime) BETWEEN :startDate AND :endDate")
        List<String> findVehicleLicensePlatesForReportInspectionBeforeDelivery(
                @Param("status") List<Integer> status, @Param("username") String username,
                @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

        @Query("SELECT v.licensePlates " +
                "FROM Vehicle v " +
                "INNER JOIN Schedule s ON v.id = s.vehicle.id " +
                "INNER JOIN Consignment c ON c.id = s.consignment.id " +
                "INNER JOIN Place p ON p.consignment.id = c.id " +
                "INNER JOIN Driver d ON d.id = s.driver.id " +
                "INNER JOIN Account a ON a.id = d.account.id " +
                "AND a.username = :username AND s.isApprove = true AND c.status IN :status " +
                "GROUP BY v.id " +
                "HAVING MIN(p.actualTime) BETWEEN :startDate AND :endDate")
        List<String> findVehicleLicensePlatesForReportInspectionAfterDelivery(
                @Param("status") List<Integer> status, @Param("username") String username,
                @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

}
