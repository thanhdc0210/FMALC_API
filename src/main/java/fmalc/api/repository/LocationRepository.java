package fmalc.api.repository;

import fmalc.api.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LocationRepository extends JpaRepository<Location, Integer> {
//    @Query("SELECT l  FROM Location l WHERE l.vehicle.id= ?1")
//    List<Location> getListLocationById(int vehicle);

    @Query("select l from Location l where l.schedule.id=?1 ")
    List<Location> findBySchedule(int id);
//    @Query("select new fmalc.api.dto.LocationDTO(l.latitude,l.longitude,l.time) from Location l where l.vehicle.id=?1")
//    List<LocationDTO> getListLocationById1(int vehicle);
}
