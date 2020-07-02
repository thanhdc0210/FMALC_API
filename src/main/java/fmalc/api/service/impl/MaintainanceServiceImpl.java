package fmalc.api.service.impl;

import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Maintenance;
import fmalc.api.repository.MaintainanceRepository;
import fmalc.api.service.MaintainanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MaintainanceServiceImpl implements MaintainanceService {

    @Autowired
    MaintainanceRepository maintainanceRepository;

    @Override
    public MaintainCheckDTO checkMaintainForVehicle(int idVehilce)  {
        List<MaintainCheckDTO> maintainCheckDTOs = new ArrayList<>();
        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();
        List<Integer> id = new ArrayList<>();
        boolean flag;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
//        date = sdf.parse(sdf.format(date));
//        java.sql.Date date1 = new java.sql.Date(date.getTime());
        List<Maintenance> maintenances = maintainanceRepository.findByVehicle(idVehilce);
        if(maintenances.size()>0){

            maintainCheckDTOs = maintainCheckDTO.mapToListResponse(maintenances);
            if(maintainCheckDTOs.size()>0){
                for(int i=0; i< maintainCheckDTOs.size();i++){
                    if(sdf.format(date).compareTo(sdf.format(maintainCheckDTOs.get(i).getMaintainDate()))>0){
                        id.add(maintainCheckDTOs.get(i).getId());
                    }
                }
                for (int j = 0; j < id.size(); j++) {
                    flag = true;
                    for (int i = 0; i < maintainCheckDTOs.size(); i++) {
                        if(flag){
                            if (maintainCheckDTOs.get(i).getId() == id.get(j)) {
                                maintainCheckDTOs.remove(maintainCheckDTOs.get(i));
                                flag = false;
                            }
                        }
                    }

                }
                if(maintainCheckDTOs.size()>0){
                    maintainCheckDTO = maintainCheckDTOs.get(0);
                }
            }


        }
        return maintainCheckDTO;
    }

    @Override
    public MaintainCheckDTO checkMaintaiinForDriver(int idDriver) {
        List<MaintainCheckDTO> maintainCheckDTOs = new ArrayList<>();
        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        boolean flag;
        List<Integer> id = new ArrayList<>();
//        date = sdf.parse(sdf.format(date));
//        java.sql.Date date1 = new java.sql.Date(date.getTime());
        List<Maintenance> maintenances = maintainanceRepository.findByDriver(idDriver);
        if(maintenances.size()>0){
            maintainCheckDTOs = maintainCheckDTO.mapToListResponse(maintenances);



                for(int i=0; i< maintainCheckDTOs.size();i++){

                    if(sdf.format(date).compareTo(sdf.format(maintainCheckDTOs.get(i).getMaintainDate()))>0){
                        id.add(maintainCheckDTOs.get(i).getId());
                    }
                }
            for (int j = 0; j < id.size(); j++) {

                flag =true;
                for (int i = 0; i < maintainCheckDTOs.size(); i++) {
                    if(flag){
                        if (maintainCheckDTOs.get(i).getId() == id.get(j)) {
                            maintainCheckDTOs.remove(maintainCheckDTOs.get(i));
                            flag = false;
                        }
                    }

                }
            }
            if(maintainCheckDTOs.size()>0){
                maintainCheckDTO = maintainCheckDTOs.get(0);
            }

        }
        return maintainCheckDTO;
    }
}
