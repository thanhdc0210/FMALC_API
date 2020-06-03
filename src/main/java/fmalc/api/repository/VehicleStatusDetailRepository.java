package fmalc.api.repository;

import fmalc.api.entities.VehicleStatusDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VehicleStatusDetailRepository extends JpaRepository<VehicleStatusDetail, Integer>, JpaSpecificationExecutor<VehicleStatusDetail> {

}