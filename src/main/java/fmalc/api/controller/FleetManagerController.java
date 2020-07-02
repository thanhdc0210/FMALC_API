package fmalc.api.controller;


import fmalc.api.dto.FLeetResponseDTO;
import fmalc.api.entity.FleetManager;
import fmalc.api.service.FleetManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/fleetmanager")
public class FleetManagerController {

    @Autowired
    FleetManagerService fleetManagerService;

    @GetMapping
    public ResponseEntity<List<FLeetResponseDTO>> getAllFleetManager(){
        try{
            FLeetResponseDTO fLeetResponseDTO = new FLeetResponseDTO();
            List<FLeetResponseDTO> fleetResponseDTOS = new ArrayList<>();
            List<FleetManager> fleetManagers = fleetManagerService.getAllFleet();
            if(fleetManagers.size()>0){
                fleetResponseDTOS = fLeetResponseDTO.convertListFleet(fleetManagers);
                return ResponseEntity.ok().body(fleetResponseDTOS);
            }else{
                return ResponseEntity.noContent().build();
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
