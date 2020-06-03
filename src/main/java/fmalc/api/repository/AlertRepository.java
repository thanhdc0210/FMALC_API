package fmalc.api.repository;

import fmalc.api.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlertRepository extends JpaRepository<Alert, Integer>, JpaSpecificationExecutor<Alert> {

}