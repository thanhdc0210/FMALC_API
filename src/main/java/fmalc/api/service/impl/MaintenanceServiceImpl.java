package fmalc.api.service.impl;

import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.entity.Maintenance;
import fmalc.api.repository.MaintainanceRepository;
import fmalc.api.service.MaintenanceService;
import fmalc.api.service.UploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintainanceRepository maintainanceRepository;
    @Autowired
    private UploaderService uploaderService;

    @Override
    public MaintainCheckDTO checkMaintainForVehicle(int idVehicle) {
        List<Maintenance> maintenances = maintainanceRepository.findByVehicle(idVehicle);
        if (!maintenances.isEmpty()) {
            List<MaintainCheckDTO> maintainCheckDTOs = new MaintainCheckDTO().mapToListResponse(maintenances);
            Date date = new Date();
            List<Integer> id = maintainCheckDTOs.stream()
                    .filter(x -> date.after(x.getPlannedMaintainDate()))
                    .map(MaintainCheckDTO::getId)
                    .collect(Collectors.toList());

            maintainCheckDTOs.removeIf(x -> id.contains(x.getId()));

            if (!maintainCheckDTOs.isEmpty()) {
                return maintainCheckDTOs.get(0);
            }
        }
        return new MaintainCheckDTO();
    }

    @Override
    public MaintainCheckDTO checkMaintainForDriver(int idDriver) {

        List<Maintenance> maintenances = maintainanceRepository.findByDriver(idDriver);
        if (!maintenances.isEmpty()) {
            List<MaintainCheckDTO> maintainCheckDTOs = new MaintainCheckDTO().mapToListResponse(maintenances);

            Date date = new Date();
            List<Integer> id = maintainCheckDTOs.stream()
                    .filter(x -> date.after(x.getPlannedMaintainDate()))
                    .map(MaintainCheckDTO::getId)
                    .collect(Collectors.toList());
            maintainCheckDTOs.removeIf(x -> id.contains(x.getId()));

            if (!maintainCheckDTOs.isEmpty()) {
                return maintainCheckDTOs.get(0);
            }

        }
        return new MaintainCheckDTO();
    }

    @Override
    public List<Maintenance> getListMaintenanceForDriver(int driverId) {

        List<Maintenance> maintenanceList = new ArrayList<>();
        maintenanceList = maintainanceRepository.findMaintenancesByDriverIdAndAndStatus(driverId, false);
        return maintenanceList;
    }

    @Override
    public Maintenance updateMaintainingComplete(int id, int km, MultipartFile file) throws IOException {
        Maintenance maintenance = maintainanceRepository.findById(id).get();
        Date currentTime = Timestamp.valueOf(LocalDateTime.now());
        if (maintenance != null) {
            Date actualTime = maintenance.getActualMaintainDate();
            if (currentTime.after(actualTime) && currentTime.before(new Date(actualTime.getTime() + (1000 * 60 * 60 * 24)))) {
                if (maintenance.getKmOld() < km) {
                    maintenance.setKmOld(km);
                    String image = uploaderService.upload(file);
                    maintenance.setImageMaintain(image);
                    maintenance.setStatus(true);
                }
            }
        }
        return maintainanceRepository.save(maintenance);
    }


}
