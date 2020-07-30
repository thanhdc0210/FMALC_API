package fmalc.api.repository;

import fmalc.api.entity.MaintenanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintainTypeRepository extends JpaRepository<MaintenanceType, Integer> {
    MaintenanceType findByMaintenanceTypeName(String maintenanceTypeName);
    MaintenanceType findByKilometersNumber(int km);
}
