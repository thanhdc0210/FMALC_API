package fmalc.api.controller;


import fmalc.api.common.ResponseStatusEnum;
import fmalc.api.entities.Driver;
import fmalc.api.entities.DriverLicense;
import fmalc.api.service.DriverLicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/license")
public class DriverLicenseController {

    @Autowired
    DriverLicenseService driverLicenseService;

    @PostMapping("/")
    public ResponseEntity<DriverLicense> createLicense(@RequestBody DriverLicense driverLicense){

        try{
            DriverLicense licenseSaved = driverLicenseService.createLicense(driverLicense);
            if(licenseSaved == null){
                return ResponseEntity.noContent().build();
            }else{
                return ResponseEntity.ok().body(licenseSaved);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/listLicense")
    public ResponseEntity<List<DriverLicense>> getListLicense(){
        List<DriverLicense> driverLicenses = driverLicenseService.getListDriverLicense();
        if(driverLicenses.isEmpty()){
            DriverLicense driverLicense = new DriverLicense();
            driverLicense.setExpires("r");
            driverLicense.setNo("540169003082");
            driverLicense.setLicenseType("C");
            driverLicense.set
//            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok().body(driverLicenses);
        }

    }

}
