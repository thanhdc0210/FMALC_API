package fmalc.api.repository;

import fmalc.api.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MaintainanceRepository extends JpaRepository<Maintenance, Integer> {
//    Maintenance findBy
    @Query("Select m from Maintenance m where m.vehicle.id =?1")
    List<Maintenance> findByVehicle(int idVehicle);

    @Query("Select m from Maintenance m where m.driver.id =?1")
    List<Maintenance> findByDriver(int idDriver);
//    Maintenan
}
