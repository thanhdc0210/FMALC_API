package fmalc.api.repository;

import fmalc.api.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository
public interface MaintainanceRepository extends JpaRepository<Maintenance, Integer> {
    //    Maintenance findBy
    @Query("Select m from Maintenance m where m.vehicle.id =?1")
    List<Maintenance> findByVehicle(int idVehicle);

    @Query("Select m from Maintenance m where m.driver.id =?1")
    List<Maintenance> findByDriver(int idDriver);

    //GiangTLB
    List<Maintenance> findMaintenancesByDriverIdAndAndStatus(int driverId, boolean status);

    @Query("update Maintenance m set m.imageMaintain=?1, m.kmOld=?2, m.status=true where m.id=?3")
    Maintenance updateMaintanance(String image,Integer kmOld, Integer id);
    //---------
}
