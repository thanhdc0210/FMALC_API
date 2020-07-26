package fmalc.api.service.impl;

import fmalc.api.controller.NotificationController;
import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.dto.MaintainReponseDTO;
import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.entity.MaintainType;
import fmalc.api.entity.Maintenance;
import fmalc.api.entity.Vehicle;
import fmalc.api.repository.MaintainTypeRepository;
import fmalc.api.repository.MaintenanceRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.MaintenanceService;
import fmalc.api.service.UploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceRepository maintainanceRepository;
    @Autowired
    private UploaderService uploaderService;

    @Autowired
    NotificationController notificationController;

    @Autowired
    MaintainTypeRepository maintainTypeRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public MaintainCheckDTO checkMaintainForVehicle(int idVehicle) {
        List<Maintenance> maintenances = maintainanceRepository.findByVehicle(idVehicle);
        if (!maintenances.isEmpty()) {
            List<MaintainCheckDTO> maintainCheckDTOs = new MaintainCheckDTO().mapToListResponse(maintenances);
            Date date = new Date(System.currentTimeMillis());
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

            Date date = new Date(System.currentTimeMillis());
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
        Date currentTime = new Date(System.currentTimeMillis());
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

    @Override
    public List<MaintainReponseDTO> getListMaintainByVehicle(int idVehicle) {
        List<Maintenance> maintenances = maintainanceRepository.findByVehicle(idVehicle);
        MaintainReponseDTO maintainReponseDTO = new MaintainReponseDTO();
        List<MaintainReponseDTO> maintainReponseDTOS = maintainReponseDTO.mapToListResponse(maintenances);
        return maintainReponseDTOS;
    }

    @Override
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
        //Increase kmRunning hardcode
        vehicleRepository.updateKmRunning(idVehicle, vehicle.getKilometerRunning() + 1000);

        long daysBetween = Duration.between(maintenance.getActualMaintainDate().toLocalDate().atStartOfDay(), today.atStartOfDay()).toDays();
        if (daysBetween == 0) daysBetween = 1;
        double averageKmOnDays = (vehicle.getKilometerRunning() - maintenance.getKmOld()) / (daysBetween);
        long dayNextMaintain = (long) Math.ceil((maintenance.getKmOld() + 5000 - vehicle.getKilometerRunning()) / averageKmOnDays);
        Date next = Date.valueOf(today.plusDays(dayNextMaintain));


        if (isUpdate) {
            maintainanceRepository.updatePlannedMaintainDate(maintenances.get(0).getId(), next);
            if (Duration.between(today.atStartOfDay(), next.toLocalDate().atStartOfDay()).toDays() <= 7) {
                maintainanceRepository.updateActualMaintainDate(maintenances.get(0).getId(), next);
                //Assign Driver
                // Notify
                NotificationRequestDTO requestDTO = new NotificationRequestDTO();
                requestDTO.setContent("Lịch bảo trì cho xe " + vehicle.getLicensePlates());
                //hardcode driver
                requestDTO.setDriver_id(1);
                requestDTO.setVehicle_id(idVehicle);
                requestDTO.setStatus(true);
                requestDTO.setType(4);
                notificationController.createNotification(requestDTO);
            }
            return;
        }

        Maintenance addMaintenance = new Maintenance();
        addMaintenance.setPlannedMaintainDate(next);
        addMaintenance.setKmOld(maintenances.get(0).getKmOld() + 5000);
        addMaintenance.setVehicle(vehicle);
        MaintainType maintainType = maintainTypeRepository.findById(1).get();
        if (maintenance.getMaintainType().getId() == 1) {
            maintainType = maintainTypeRepository.findById(2).get();
        }
        addMaintenance.setMaintainType(maintainType);
        maintainanceRepository.save(addMaintenance);
        if (Duration.between(today.atStartOfDay(), next.toLocalDate().atStartOfDay()).toDays() <= 7) {
            maintainanceRepository.updateActualMaintainDate(maintenances.get(0).getId(), next);
            //Assign Driver
            // Notify
            NotificationRequestDTO requestDTO = new NotificationRequestDTO();
            requestDTO.setContent("Lịch bảo trì cho xe " + vehicle.getLicensePlates());
            //hardcode driver
            requestDTO.setDriver_id(1);
            requestDTO.setVehicle_id(idVehicle);
            requestDTO.setStatus(true);
            requestDTO.setType(4);
            notificationController.createNotification(requestDTO);
        }
    }

    @Override
    public List<Maintenance> getMaintenance() {
        return maintainanceRepository.findAllByActualMaintainDateIsNotNullOrderByActualMaintainDateDesc();
    }

    @Override
    public void createFirstMaintain(Vehicle vehicle) {
        LocalDate today = LocalDate.now();
        Maintenance addMaintenance = new Maintenance();
        addMaintenance.setPlannedMaintainDate(Date.valueOf(today));
        addMaintenance.setActualMaintainDate(Date.valueOf(today));
        addMaintenance.setKmOld(vehicle.getKilometerRunning());
        addMaintenance.setVehicle(vehicle);
        MaintainType maintainType = maintainTypeRepository.findById(1).get();
        addMaintenance.setMaintainType(maintainType);
        maintainanceRepository.save(addMaintenance);
    }
}
