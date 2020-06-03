package fmalc.api.repository;

import fmalc.api.entities.MaintainType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MaintainTypeRepository extends JpaRepository<MaintainType, Integer>, JpaSpecificationExecutor<MaintainType> {

}