package fmalc.api.repository;

import fmalc.api.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {
    Page findAllByOrderByIdDesc(Pageable pa);

    @Query("select al from  Alert  al, Account a, Driver d, FleetManager  f where al.driver.id = d.id and " +
            " d.fleetManager.id = f.id and a.id = f.account.id and a.username=?1" )
    Page findAlertByDriver(String username, Pageable pa);
}
