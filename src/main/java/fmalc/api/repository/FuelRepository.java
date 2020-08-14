package fmalc.api.repository;

import fmalc.api.entity.Fuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface FuelRepository extends JpaRepository<Fuel, Integer> {
        @Query("select f from Fuel f where f.vehicle.id =?1")
        List<Fuel> getByVehicle(int idVehicle);
}
