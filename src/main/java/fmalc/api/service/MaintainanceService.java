package fmalc.api.service;

import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.dto.MaintainReponseDTO;
import fmalc.api.entity.Maintenance;

import java.util.List;

public interface MaintainanceService {
    MaintainCheckDTO checkMaintainForVehicle(int idVehicle);

    MaintainCheckDTO checkMaintaiinForDriver( int idDriver);

    List<MaintainReponseDTO> getListMaintainByVehicle(int idVehicle);

    void calculateMaintenanceForVehicle(int idVehicle);

    List<Maintenance> getMaintenance();
}
