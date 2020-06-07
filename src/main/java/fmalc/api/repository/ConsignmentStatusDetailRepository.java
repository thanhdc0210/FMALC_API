package fmalc.api.repository;

import fmalc.api.entities.ConsignmentStatusDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ConsignmentStatusDetailRepository extends JpaRepository<ConsignmentStatusDetail, Integer>, JpaSpecificationExecutor<ConsignmentStatusDetail> {

}