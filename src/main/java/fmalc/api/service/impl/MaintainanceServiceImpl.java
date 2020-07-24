package fmalc.api.service.impl;

import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.dto.MaintainReponseDTO;
import fmalc.api.entity.Maintenance;
import fmalc.api.entity.Vehicle;
import fmalc.api.repository.MaintainanceRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.MaintainanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintainanceServiceImpl implements MaintainanceService {

    @Autowired
    MaintainanceRepository maintainanceRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public MaintainCheckDTO checkMaintainForVehicle(int idVehicle) {
        List<Maintenance> maintenances = maintainanceRepository.findByVehicle(idVehicle);
        if (!maintenances.isEmpty()) {
            List<MaintainCheckDTO> maintainCheckDTOs = new MaintainCheckDTO().mapToListResponse(maintenances);
            java.util.Date date = new java.util.Date();
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
    public MaintainCheckDTO checkMaintaiinForDriver(int idDriver) {

        List<Maintenance> maintenances = maintainanceRepository.findByDriver(idDriver);
        if (!maintenances.isEmpty()) {
            List<MaintainCheckDTO> maintainCheckDTOs = new MaintainCheckDTO().mapToListResponse(maintenances);

            java.util.Date date = new java.util.Date();
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
    public List<MaintainReponseDTO> getListMaintainByVehicle(int idVehicle) {
        List<Maintenance> maintenances = maintainanceRepository.findByVehicle(idVehicle);
        MaintainReponseDTO maintainReponseDTO = new MaintainReponseDTO();
        List<MaintainReponseDTO> maintainReponseDTOS = maintainReponseDTO.mapToListResponse(maintenances);
        return maintainReponseDTOS;
    }

    public void calculateMaintenanceForVehicle(int idVehicle) {
        List<Maintenance> maintenances = maintainanceRepository.findTop2ByVehicle_IdOrderByIdDesc(idVehicle);
        LocalDate today = LocalDate.now();
        Maintenance maintenance = maintenances.get(0);
        if (maintenance.getActualMaintainDate() != null && maintenance.getActualMaintainDate().toLocalDate().isAfter(today)) {
            return;
        }
        boolean isUpdate = true;
        if (maintenances.size() == 1 || maintenance.getActualMaintainDate() != null) {
            isUpdate = false;
        } else {
            maintenance = maintenances.get(1);
        }

        Vehicle vehicle = maintenance.getVehicle();

        long daysBetween = Duration.between(maintenance.getActualMaintainDate().toLocalDate().atStartOfDay(), today.atStartOfDay()).toDays();

        double averageKmOnDays = (vehicle.getKilometerRunning() - maintenance.getKmOld()) / (daysBetween);
        long dayNextMaintain = (long) Math.ceil((maintenance.getKmOld() + 5000 - vehicle.getKilometerRunning()) / averageKmOnDays);
        Date next = Date.valueOf(today.plusDays(dayNextMaintain));

        if (isUpdate) {
            maintainanceRepository.updatePlannedMaintainDate(maintenances.get(0).getId(), next);
            if (Duration.between(today.atStartOfDay(), next.toLocalDate().atStartOfDay()).toDays() <= 7) {
                maintainanceRepository.updateActualMaintainDate(maintenances.get(0).getId(), next);
                //Assign Driver
            }
            return;
        }

        Maintenance addMaintenance = new Maintenance();
        addMaintenance.setPlannedMaintainDate(next);
        addMaintenance.setKmOld(maintenances.get(0).getKmOld() + 5000);
        addMaintenance.setVehicle(vehicle);
        addMaintenance.setMaintainType(maintenance.getMaintainType());

        maintainanceRepository.save(addMaintenance);
    }

    @Override
    public List<Maintenance> getMaintenance() {
        return maintainanceRepository.findAllByActualMaintainDateIsNotNullOrderByActualMaintainDateDesc();
    }
}
