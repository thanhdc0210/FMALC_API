package fmalc.api.repository;



import fmalc.api.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
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

//    @Query("Select DISTINCT v.licensePlates " +
//            "From Consignment c, Schedule s, Driver d, Place p, Vehicle v " +
//            "Where c.id = s.consignment.id AND d.id = s.driver.id AND v.id = s.vehicle.id " +
//            "AND c.status IN :status AND d.id = :id  AND p.plannedTime <= :currentDate")
//    List<String> findVehicleLicensePlatesForReportInspection(@Param("status") List<Integer> status, @Param("id") Integer driver_id, @Param("currentDate") Timestamp currentDate);

        @Query("Select DISTINCT v.licensePlates " +
                "From Consignment c, Schedule s, Driver d, Place p, Vehicle v, Account a " +
                "Where c.id = s.consignment.id AND d.id = s.driver.id AND v.id = s.vehicle.id " +
                "AND a.id = d.account.id "+
                "AND c.status IN :status AND a.username = :username and s.isApprove = true " +
                "AND DATE(p.plannedTime) = :currentDate")
        List<String> findVehicleLicensePlatesForReportInspectionBeforeDelivery(
                @Param("status") List<Integer> status, @Param("username") String username
                , @Param("currentDate") Date currentDate);

        @Query("Select DISTINCT v.licensePlates " +
                "From Consignment c, Schedule s, Driver d, Place p, Vehicle v, Account a " +
                "Where c.id = s.consignment.id AND d.id = s.driver.id AND v.id = s.vehicle.id " +
                "AND a.id = d.account.id "+
                "AND c.status IN :status AND a.username = :username and s.isApprove = true " +
                "AND p.actualTime <= :currentDate")
        List<String> findVehicleLicensePlatesForReportInspectionAfterDelivery(@Param("status") List<Integer> status, @Param("username") String username, @Param("currentDate") Date currentDate);

}
