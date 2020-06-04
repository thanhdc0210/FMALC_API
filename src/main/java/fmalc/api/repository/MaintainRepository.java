package fmalc.api.repository;

import fmalc.api.entities.Maintain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface MaintainRepository extends JpaRepository<Maintain, Integer>, JpaSpecificationExecutor<Maintain> {

}