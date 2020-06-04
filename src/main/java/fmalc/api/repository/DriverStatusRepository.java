package fmalc.api.repository;

import fmalc.api.entities.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface DriverStatusRepository extends JpaRepository<DriverStatus, Integer>, JpaSpecificationExecutor<DriverStatus> {

}