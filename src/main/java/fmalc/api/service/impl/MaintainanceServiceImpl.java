package fmalc.api.service.impl;

import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.entity.Maintenance;
import fmalc.api.repository.MaintainanceRepository;
import fmalc.api.service.MaintainanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintainanceServiceImpl implements MaintainanceService {

    @Autowired
    MaintainanceRepository maintainanceRepository;

    @Override
    public MaintainCheckDTO checkMaintainForVehicle(int idVehicle)  {
        List<Maintenance> maintenances = maintainanceRepository.findByVehicle(idVehicle);
        if(!maintenances.isEmpty()) {
            List<MaintainCheckDTO> maintainCheckDTOs = new MaintainCheckDTO().mapToListResponse(maintenances);
            Date date = new Date();
            List<Integer> id = maintainCheckDTOs.stream()
                    .filter(x -> date.after(x.getPlannedMaintainDate()))
                    .map(MaintainCheckDTO::getId)
                    .collect(Collectors.toList());

            maintainCheckDTOs.removeIf(x -> id.contains(x.getId()));

            if(!maintainCheckDTOs.isEmpty()){
                return maintainCheckDTOs.get(0);
            }
        }
        return new MaintainCheckDTO();
    }

    @Override
    public MaintainCheckDTO checkMaintaiinForDriver(int idDriver) {

        List<Maintenance> maintenances = maintainanceRepository.findByDriver(idDriver);
        if(!maintenances.isEmpty()) {
            List<MaintainCheckDTO> maintainCheckDTOs = new MaintainCheckDTO().mapToListResponse(maintenances);

            Date date = new Date();
            List<Integer> id = maintainCheckDTOs.stream()
                                                .filter(x -> date.after(x.getPlannedMaintainDate()))
                                                .map(MaintainCheckDTO::getId)
                                                .collect(Collectors.toList());
            maintainCheckDTOs.removeIf(x -> id.contains(x.getId()));

            if(!maintainCheckDTOs.isEmpty()){
                return maintainCheckDTOs.get(0);
            }

        }
        return new MaintainCheckDTO();
    }
}
