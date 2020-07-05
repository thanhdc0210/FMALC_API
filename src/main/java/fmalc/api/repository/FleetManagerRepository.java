package fmalc.api.repository;

import fmalc.api.entity.FleetManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@Repository
public interface FleetManagerRepository extends JpaRepository<FleetManager, Integer> {
    @Modifying
    @Transactional
    @Query(value = "Update fleet_manager f set f.name = :name, f.identity_no = :identityNo, f.date_of_birth = :dateOfBirth  where f.id =:id", nativeQuery = true)
    void updateFleetManager(Integer id, String name, String identityNo, Date dateOfBirth);
}
