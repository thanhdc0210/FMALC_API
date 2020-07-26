package fmalc.api.controller;
import fmalc.api.dto.MaintainReponseDTO;
import fmalc.api.dto.MaintainanceResponse;
import fmalc.api.dto.MaintenanceResponseDTO;
import fmalc.api.entity.Maintenance;
import fmalc.api.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/maintenances")
public class MaintenanceController {

    @Autowired
    MaintenanceService maintenanceService;

    @GetMapping("/actual-date")
    public ResponseEntity getMaintenance() {
        try {
            List<Maintenance> maintenances = maintenanceService.getMaintenance();
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
