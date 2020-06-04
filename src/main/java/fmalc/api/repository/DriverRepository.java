package fmalc.api.repository;

import fmalc.api.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface DriverRepository extends JpaRepository<Driver, Integer>, JpaSpecificationExecutor<Driver> {

}