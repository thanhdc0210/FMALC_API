package fmalc.api.repository;

import fmalc.api.entity.FleetManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FleetManagerRepository extends JpaRepository<FleetManager, Integer> {
    @Modifying
    @Transactional
    @Query(value = "Update fleet_manager f set f.name = :name, f.identity_no = :identityNo  where d.id =:id", nativeQuery = true)
    void updateFleetManager(Integer id, String name, String identityNo);
}
