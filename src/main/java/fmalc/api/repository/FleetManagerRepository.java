package fmalc.api.repository;

import fmalc.api.entities.FleetManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FleetManagerRepository extends JpaRepository<FleetManager, Integer> {
}
