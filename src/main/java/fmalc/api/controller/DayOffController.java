package fmalc.api.controller;

import fmalc.api.dto.DayOffDTO;
import fmalc.api.service.DayOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1.0/dayoff")
public class DayOffController {

    @Autowired
    DayOffService dayOffService;

    @PostMapping()
    public ResponseEntity<Boolean> confirmDayOff(@RequestBody DayOffDTO dayOffDTO){
        try{
            boolean result = dayOffService.confirmDayOff(dayOffDTO);
            if(result){
                return  ResponseEntity.ok().body(result);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return  ResponseEntity.noContent().build();
    }
}
