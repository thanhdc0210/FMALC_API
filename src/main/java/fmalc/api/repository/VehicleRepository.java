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

        @Modifying(clearAutomatically = true)
        @Transactional
        @Query(value = "UPDATE Vehicle v set v.status = ?1 where v.id = ?2", nativeQuery = true)
        void updateStatusVehicle(int status, int id);

    @Query("Select DISTINCT v.licensePlates " +
            "From Consignment c, Account a, Schedule s, Driver d, Place p, Vehicle v " +
            "Where a.id = d.account.id " +
            "AND c.id = s.consignment.id AND d.id = s.driver.id AND v.id = s.vehicle.id " +
            "AND c.status IN :status AND a.username = :username  AND p.plannedTime <= :currentDate")
    List<String> findVehicleLicensePlatesForReportInspection(@Param("status") List<Integer> status, @Param("username") String username, @Param("currentDate") Timestamp currentDate);

}
