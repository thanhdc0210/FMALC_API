package fmalc.api.repository;

import fmalc.api.entity.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public interface ConsignmentRepository
        extends JpaRepository<Consignment, Integer>, JpaSpecificationExecutor<Consignment> {

//    @Query("Select c From Consignment c, Account a, ConsignmentHistory ch, FleetManager fm" +
//            " Where c.id = ch.consignment.id and ch.fleetManager.id = fm.id" +
//            " and fm.account.id = a.id and c.status IN :status and a.username = :username" +
//            " and c.schedule.isApprove = true")
//    List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(@Param("status") List<Integer> status, @Param("username") String username);

    List<Consignment> findAllByStatus(Integer status);

//    @Query("select  c from  c")
//    List<Consignment> getlll(Integer status);

    Consignment findConsignmentById(int id);

    @Query("select c from Consignment c, Schedule s where c.id = s.consignment.id and s.vehicle.id=?1")
    List<Consignment> findConsignment(int idVehicle);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE consignment c set c.status = ?1 where c.id = ?2", nativeQuery = true)
    int updateStatusVehicle(int status, int id);

    @Query("SELECT c FROM Consignment c" +
            " INNER JOIN Schedule s ON c.id = s.consignment.id" +
            " INNER JOIN Driver d ON s.driver.id = d.id" +
            " WHERE d.id = :driverId" +
            " AND c.status = :status")
    List<Consignment> getConsignmentOfDriver(@Param("driverId") int driverId,@Param("status") int status);

    @Query(value = "SELECT c from  Consignment c where c.id in (" +
                    "SELECT p.consignment.id from Place p "+
                    "WHERE p.actualTime between :startDate AND :endDate "+
                    "GROUP BY p.consignment.id ) " +
                    " AND  c.status= :status ")
    List<Consignment> getConsignmentForReport (@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate, @Param("status") Integer status);

}
