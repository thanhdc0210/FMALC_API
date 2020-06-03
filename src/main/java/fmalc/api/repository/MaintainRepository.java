package fmalc.api.repository;

import fmalc.api.entities.Maintain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MaintainRepository extends JpaRepository<Maintain, Integer>, JpaSpecificationExecutor<Maintain> {

}