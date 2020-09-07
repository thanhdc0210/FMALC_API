package fmalc.api.repository;

import fmalc.api.entity.RefuelHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RefuelHistoryRepository extends JpaRepository<RefuelHistory, Integer> {
        @Query("select f from RefuelHistory f where f.vehicle.id =?1")
        List<RefuelHistory> getByVehicle(int idVehicle);
}
