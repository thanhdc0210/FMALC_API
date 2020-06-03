package fmalc.api.repository;

import fmalc.api.entities.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Integer>, JpaSpecificationExecutor<VehicleType> {

}