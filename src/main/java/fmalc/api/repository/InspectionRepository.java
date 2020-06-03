package fmalc.api.repository;

import fmalc.api.entities.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InspectionRepository extends JpaRepository<Inspection, Integer>, JpaSpecificationExecutor<Inspection> {

}