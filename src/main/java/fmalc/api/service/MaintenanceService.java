package fmalc.api.service;

import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.dto.MaintainReponseDTO;
import fmalc.api.entity.Maintenance;
import fmalc.api.entity.Vehicle;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MaintenanceService {
    MaintainCheckDTO checkMaintainForVehicle(int idVehicle) ;
    MaintainCheckDTO checkMaintainForDriver(int idDriver);
    List<MaintainReponseDTO> getListMaintainByVehicle(int idVehicle);
    List<Maintenance> getMaintenance();

    //GiangTLB
    List<Maintenance> getListMaintenanceForDriver(int driverId);
    //--------
    Maintenance updateMaintainingComplete(int id, int km, MultipartFile file) throws IOException;

    void createFirstMaintain(Vehicle vehicle);

    void calculateMaintenanceForVehicle(int idVehicle);
}
