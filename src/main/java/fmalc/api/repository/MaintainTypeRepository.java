package fmalc.api.repository;

import fmalc.api.entity.MaintainType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintainTypeRepository extends JpaRepository<MaintainType, Integer> {
    MaintainType findByMaintainTypeName(String maintainTypeName);
}
