package fmalc.api.controller;

import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.dto.MaintainReponseDTO;
import fmalc.api.service.MaintainanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/v1.0/maintain")
public class MaintainController {

    @Autowired
    MaintainanceService maintainanceService;

    @GetMapping("/id/{id}")
    public ResponseEntity<List<MaintainReponseDTO>> getAllMaintainForVehicle(@PathVariable int id){
       try{
           List<MaintainReponseDTO> maintainReponseDTOS = maintainanceService.getListMaintainByVehicle(id);
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
