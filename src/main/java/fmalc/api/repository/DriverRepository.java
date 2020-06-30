package fmalc.api.repository;

import fmalc.api.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    @Query("select d from  Driver d where d.driverLicense >= ?1")
    List<Driver> findByDriverLicenseC(int license);

    @Query("select d from  Driver d where d.driverLicense <= ?1")
    List<Driver> findByDriverLicenseB2(int license);
}
