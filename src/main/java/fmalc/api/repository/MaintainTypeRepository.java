package fmalc.api.repository;

import fmalc.api.entities.MaintainType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface MaintainTypeRepository extends JpaRepository<MaintainType, Integer>, JpaSpecificationExecutor<MaintainType> {

}