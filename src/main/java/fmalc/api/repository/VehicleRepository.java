package fmalc.api.repository;


import fmalc.api.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.sql.Date;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    @Query("SELECT v from Vehicle v where v.isActive =?1 ")
    List<Vehicle> getListVehicle(Boolean isActive);

    @Query("SELECT v FROM Vehicle v WHERE v.id = ?1")
    Vehicle findByIdVehicle(int id);

    @Query("SELECT v FROM Vehicle v WHERE v.licensePlates = ?1")
    Vehicle findByLicensePlates(String license);

    @Query("SELECT v FROM Vehicle v WHERE v.status = ?1 and v.weight >= ?2")
    List<Vehicle> findByStatus(int status, double weight);

    @Query("SELECT v FROM Vehicle v where  v.weight >= ?1 and v.isActive=?2")
    List<Vehicle> findByWeight(double weight, Boolean isActive);

    @Query("SELECT v FROM Vehicle v where  v.weight > ?1")
    List<Vehicle> findByWeightBigger(double weight);

    @Query("SELECT v FROM Vehicle v where  v.weight < ?1")
    List<Vehicle> findByWeightSmaller(double weight);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE vehicle v set v.status = ?1 where v.id = ?2", nativeQuery = true)
    int updateStatusVehicle(int status, int id);


//        @Modifying(clearAutomatically = true)
//        @Transactional
//        @Query(value = "UPDATE vehicle v set v.status = ?1 where v.id = ?2", nativeQuery = true)
//        int updateKVehicle(int status, int id);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = "Update vehicle v set v.kilometer_running =:kmRunning where v.id =:id", nativeQuery = true)
    void updateKmRunning(@Param("id") int id, @Param("kmRunning") int kmRunning);

    Vehicle findByIdEqualsAndStatusIsNotLike(Integer id, Integer status);

    List<Vehicle> findByDateCreateBefore(Date dateBefore);

    boolean existsByLicensePlates(String licensePlates);
//    findByPhoneNumberContainingIgnoreCaseOrderByIdDesc
    @Query("select v from Vehicle  v where v.licensePlates LIKE CONCAT('%',?2,'%') and v.status=?1")
    Page findAllLicenseStatus(int status, String license, Pageable pa);

    @Query( "select v from Vehicle  v where v.licensePlates LIKE CONCAT('%',?2,'%') and v.status <?1 ")
    Page findAllLicense( int status,String license, Pageable pa);

    @Query("select v from Vehicle  v  where v.status<?1")
    Page findAllStatusDiffUnavai(int status,Pageable pa);

    @Query("select v from Vehicle  v where v.status =?1 ")
    Page findAllStatus( int status,Pageable pa);
}
