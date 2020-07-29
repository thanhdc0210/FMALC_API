package fmalc.api.repository;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
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

    @Query("select c from Consignment c, Schedule s where c.id = s.consignment.id and s.vehicle.id=?1")
    List<Consignment> findConsignemnt(int idVehicle);
    Consignment findById (int id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE Consignment c set c.status = ?1 where c.id = ?2", nativeQuery = true)
    int updateStatusVehicle(int status, int id);

}
