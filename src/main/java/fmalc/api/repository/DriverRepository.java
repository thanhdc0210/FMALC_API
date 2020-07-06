package fmalc.api.repository;

import fmalc.api.entity.Driver;
//import fmalc.api.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Date;
import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    @Query("select d from  Driver d where d.driverLicense >= ?1 and d.status = ?2")
    List<Driver> findByDriverLicenseC(int license, int status);

    @Query("select d from  Driver d where d.driverLicense <= ?1 and d.status = ?2")
    List<Driver> findByDriverLicenseB2(int license, int status);

    Driver findById(int id);

    @Modifying
    @Transactional
    @Query(value = "Update Driver d set d.status =:status where d.id =:id", nativeQuery = true)
    int updateStatusDriver(@Param("status") int status, @Param("id")int id);

    @Modifying
    @Transactional
    @Query(value = "Update driver d set d.name = :name, d.identity_no = :identityNo, d.no = :no, d.license_expires =:licenseExpires, d.date_of_birth = :dateOfBirth where d.id =:id", nativeQuery = true)
    int updateDriver(Integer id, String name, String identityNo, String no, Date licenseExpires, Date dateOfBirth);
}
