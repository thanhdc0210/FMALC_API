package fmalc.api.service;

import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Maintenance;

import java.text.ParseException;

public interface MaintainanceService {
    MaintainCheckDTO checkMaintainForVehicle(int idVehicle) ;
    MaintainCheckDTO checkMaintaiinForDriver( int idDriver);
}
