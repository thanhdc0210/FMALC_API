package fmalc.api.repository;

import ch.qos.logback.core.boolex.EvaluationException;
import fmalc.api.dto.LocationDTO;

import fmalc.api.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface LocationRepository extends JpaRepository<Location, Integer> {
//    @Query("SELECT l  FROM Location l WHERE l.vehicle.id= ?1")
//    List<Location> getListLocationById(int vehicle);

//    @Query("select new fmalc.api.dto.LocationDTO(l.latitude,l.longitude,l.time) from Location l where l.vehicle.id=?1")
//    List<LocationDTO> getListLocationById1(int vehicle);
}
