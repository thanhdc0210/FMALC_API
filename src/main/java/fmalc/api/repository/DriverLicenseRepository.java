package fmalc.api.repository;

import fmalc.api.entities.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DriverLicenseRepository extends JpaRepository<DriverLicense, Integer>, JpaSpecificationExecutor<DriverLicense> {

}