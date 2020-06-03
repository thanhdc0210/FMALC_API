package fmalc.api.repository;

import fmalc.api.entities.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, Integer>, JpaSpecificationExecutor<DeliveryDetail> {

}