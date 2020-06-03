package fmalc.api.repository;

import fmalc.api.entities.ConsignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConsignmentStatusRepository extends JpaRepository<ConsignmentStatus, Integer>, JpaSpecificationExecutor<ConsignmentStatus> {

}