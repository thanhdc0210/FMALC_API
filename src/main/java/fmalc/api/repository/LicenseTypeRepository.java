package fmalc.api.repository;

import fmalc.api.entities.LicenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LicenseTypeRepository extends JpaRepository<LicenseType, Integer>, JpaSpecificationExecutor<LicenseType> {

}