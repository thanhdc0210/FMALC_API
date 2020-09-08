package fmalc.api.service;

import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.dto.MaintainConfirmDTO;
import fmalc.api.dto.MaintainReponseDTO;
import fmalc.api.dto.Paging;
import fmalc.api.entity.Maintenance;
import fmalc.api.entity.Vehicle;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface MaintenanceService {
    List<MaintainCheckDTO> checkMaintainForVehicle(int idVehicle) ;
    List<MaintainCheckDTO> checkMaintainForDriver(int idDriver);
    List<MaintainReponseDTO> getListMaintainByVehicle(int idVehicle);
    Paging getMaintenance(String username, int page);

    MaintainReponseDTO getDetailMaintainance(int id);

    //GiangTLB
    List<Maintenance> getListMaintenanceForDriver(int driverId);
    //--------
    Maintenance updateMaintainingComplete(int id, int km, MultipartFile file) throws IOException;

    MaintainConfirmDTO updatePlannedTime(int id , int km);

    void createFirstMaintain(Vehicle vehicle);

    List<Date> dateConfirm(int idVehicle, int idDriver, Date date);

    void calculateMaintenanceForVehicle(int idVehicle);

    List<Maintenance> getMaintenanceListForConfirm();

    // ThanhDC
//    Integer countMaintenanceScheduleNumberInADayOfDriver(Integer driverId,
//                                                         Timestamp startDate, Timestamp endDate);
}
