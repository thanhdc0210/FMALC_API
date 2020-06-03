package fmalc.api.repository;

import fmalc.api.entities.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConsignmentRepository extends JpaRepository<Consignment, Integer>, JpaSpecificationExecutor<Consignment> {

}