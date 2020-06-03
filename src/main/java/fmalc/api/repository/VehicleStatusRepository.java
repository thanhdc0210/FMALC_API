package fmalc.api.repository;

import fmalc.api.entities.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VehicleStatusRepository extends JpaRepository<VehicleStatus, Integer>, JpaSpecificationExecutor<VehicleStatus> {

}