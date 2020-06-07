package fmalc.api.repository;

import fmalc.api.entities.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, Integer>, JpaSpecificationExecutor<DeliveryDetail> {

}