package fmalc.api.repository;

import fmalc.api.entity.Maintenance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    //    Maintenance findBy
    @Query("Select m from Maintenance m where m.vehicle.id =?1")
    List<Maintenance> findByVehicle(int idVehicle);

    @Query("Select m from Maintenance m where m.driver.id =?1")
    List<Maintenance> findByDriver(int idDriver);
    List<Maintenance> findTop2ByVehicle_IdOrderByIdDesc(int vehicleId);



    @Modifying
    @Transactional
    @Query(value = "Update maintenance m set m.planned_maintain_date =:date where m.id =:id", nativeQuery = true)
    void updatePlannedMaintainDate(Integer id, Date date);

    @Modifying
    @Transactional
    @Query(value = "Update maintenance m set m.actual_maintain_date =:date where m.id =:id", nativeQuery = true)
    void updateActualMaintainDate(Integer id, Date date);


    @Query("select m from Maintenance m, Driver d where m.driver.id =d.id ")
    Page<Maintenance> findAllByActualMaintainDateIsNotNullOrderByIdDesc(Pageable pa);

    @Query("select m from Maintenance m, Driver d,  FleetManager f where m.driver.id =d.id and d.fleetManager.id = f.id and f.id=?1")
    Page<Maintenance> findAllByAccount(int idFleet,Pageable pa);

    List<Maintenance> findAllByActualMaintainDateIsNotNullAndStatusIsFalseOrderByIdDesc();

    //GiangTLB
    List<Maintenance> findMaintenancesByDriverIdAndAndStatus(int driverId, boolean status);
    @Query("select m from Maintenance m where m.vehicle.id =?1 and m.status=?2")
    Maintenance findByIdVehicleAndStatus(int idVehicle, Boolean status);

    Maintenance findByIdAndStatus(int id, boolean status);
    List<Maintenance> findByStatus(boolean status);
    // ThanhDC
//    @Query("Select count(id) from Maintenance m where m.driver.id = :id " +
//            "And m.actualMaintainDate between :startDate and :endDate")
//    Integer countMaintenanceScheduleNumberInADayOfDriver(@Param("id") Integer id,
//                                                         @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}
