package fmalc.api.repository;

import fmalc.api.entity.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ConsignmentRepository
        extends JpaRepository<Consignment, Integer>, JpaSpecificationExecutor<Consignment> {

    @Query("Select c From Consignment c, Schedule s, Account a, Driver d" +
            " Where d.account.id = a.id and s.driver.id = d.id and s.consignment.id = c.id" +
            " and c.status IN :status and a.username = :username")
    List<Consignment> findByConsignmentStatusAndUsernameForDriver(@Param("status") List<Integer> status, @Param("username") String username);

    @Query("Select c From Consignment c, Account a, ConsignmentHistory ch, FleetManager fm" +
            " Where c.id = ch.consignment.id and ch.fleetManager.id = fm.id" +
            " and fm.account.id = a.id and c.status IN :status and a.username = :username")
    List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(@Param("status") List<Integer> status, @Param("username") String username);

    List<Consignment> findAllByStatus(Integer status);
}
