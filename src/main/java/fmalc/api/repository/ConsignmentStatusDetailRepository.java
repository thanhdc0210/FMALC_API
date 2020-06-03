package fmalc.api.repository;

import fmalc.api.entities.ConsignmentStatusDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConsignmentStatusDetailRepository extends JpaRepository<ConsignmentStatusDetail, Integer>, JpaSpecificationExecutor<ConsignmentStatusDetail> {

}