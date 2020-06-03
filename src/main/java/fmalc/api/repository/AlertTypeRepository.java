package fmalc.api.repository;

import fmalc.api.entities.AlertType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlertTypeRepository extends JpaRepository<AlertType, Integer>, JpaSpecificationExecutor<AlertType> {

}