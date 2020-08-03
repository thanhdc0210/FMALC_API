package fmalc.api.controller;
import fmalc.api.dto.MaintainConfirmDTO;
import fmalc.api.dto.MaintainReponseDTO;
import fmalc.api.dto.MaintainanceResponse;
import fmalc.api.dto.MaintenanceResponseDTO;
import fmalc.api.entity.Maintenance;
import fmalc.api.entity.Vehicle;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.MaintenanceService;
import fmalc.api.service.VehicleService;
import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/api/v1.0/maintenances")
public class MaintenanceController {

    @Autowired
    MaintenanceService maintenanceService;

    @Autowired
//    VehicleRepository vehicleRepository;
    VehicleService vehicleService;

    @GetMapping("/actual-date")
    public ResponseEntity getMaintenance() {
        try {
            List<Maintenance> maintenances = maintenanceService.getMaintenance();
            var idsRemove = maintenances.stream()
                    .collect(groupingBy(x -> x.getVehicle().getId(),
                            minBy(comparingInt(y -> y.getId()))))
                    .values()
                    .stream()
                    .map(x -> x.get().getId())
                    .collect(toList());
            maintenances.removeIf(x -> idsRemove.contains(x.getId()));
            if (maintenances.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                List<MaintainanceResponse> result = new ArrayList<>(new MaintainanceResponse().mapToListResponse(maintenances));
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<MaintenanceResponseDTO>> getListMaintenanceForDriver(@RequestParam Integer driverId) {
        List<Maintenance> maintenanceList = maintenanceService.getListMaintenanceForDriver(driverId);
        try {
            if (maintenanceList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                List<MaintenanceResponseDTO> result = new MaintenanceResponseDTO().mapToListResponse(maintenanceList);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping(value = "update-maintaining-complete")
    public ResponseEntity updateMaintainingComplete(@RequestParam("id") Integer id, @RequestParam("km") Integer km, @RequestPart(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Maintenance maintenance = maintenanceService.updateMaintainingComplete(id, km, file);
            if (maintenance != null) {
                return ResponseEntity.ok().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping(value = "update-plannedtime")
    public ResponseEntity<MaintainConfirmDTO> updatePlannedtime(@RequestParam("id") Integer id, @RequestParam("km") Integer km) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
        Boolean reuslt = false;
        MaintainConfirmDTO maintenance = new MaintainConfirmDTO();
        try {
            Vehicle vehicle = vehicleService.updateKmVehicle(id,km);

            maintenance = maintenanceService.updatePlannedTime(id, km);
            if(maintenance.getMaintainReponseDTO()!= null && maintenance.getMaintainReponseDTO().getActualMaintainDate()!= null){
                reuslt = true;
            }else if(maintenance!= null && maintenance.getMaintainReponseDTO().getActualMaintainDate()== null){
                reuslt = false;
            }
//            if (maintenance != null) {

//            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(maintenance);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<List<MaintainReponseDTO>> getAllMaintainForVehicle(@PathVariable int id){
        try{
            List<MaintainReponseDTO> maintainReponseDTOS = maintenanceService.getListMaintainByVehicle(id);
            if(maintainReponseDTOS.size()>0){
                return ResponseEntity.ok().body(maintainReponseDTOS);

            }else{
                return ResponseEntity.noContent().build();
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
