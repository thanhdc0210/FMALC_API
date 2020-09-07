package fmalc.api.repository;

import fmalc.api.entity.FuelHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface FuelHistoryRepository extends JpaRepository<FuelHistory, Integer> {
        @Query("select f from FuelHistory f where f.vehicle.id =?1")
        List<FuelHistory> getByVehicle(int idVehicle);
}
