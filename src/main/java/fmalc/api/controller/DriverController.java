
package fmalc.api.controller;

import fmalc.api.dto.DayOffRequestDTO;
import fmalc.api.dto.DriverRequestDTO;
import fmalc.api.dto.DriverResponseDTO;
import fmalc.api.entity.Account;
import fmalc.api.entity.Driver;
import fmalc.api.entity.FleetManager;
import fmalc.api.enums.GlobalVariables;
import fmalc.api.service.AccountService;
import fmalc.api.service.DriverService;
import fmalc.api.service.FleetManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/drivers")
public class DriverController {

    @Autowired
    DriverService driverService;

    @Autowired
    AccountService accountService;

    @Autowired
    FleetManagerService fleetManagerService;

    @GetMapping
    public ResponseEntity<List<DriverResponseDTO>> getAllDriver(@RequestParam(value = "searchPhone", defaultValue = "") String searchPhone
        , @RequestParam(value = "manager") String username
    ) {
        List<Driver> drivers = new ArrayList<>();
        Account account = accountService.getAccount(username);
        if(account.getRole().getRole().equals(GlobalVariables.ADMIN)){
             drivers = driverService.findAllAndSearch(searchPhone);
            if (drivers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        }else if(account.getRole().getRole().equals(GlobalVariables.FLEET) ){
            FleetManager fleetManager = fleetManagerService.findByAccount(account.getId());
            drivers = driverService.findAllAndSearchByFleet(fleetManager.getId(),searchPhone);
        }

        List<DriverResponseDTO> result = new ArrayList<>(new DriverResponseDTO().mapToListResponse(drivers));
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<DriverResponseDTO> findById(@PathVariable("id") Integer id) {
        Driver driver = driverService.findById(id);
        if (driver == null) {
            return ResponseEntity.badRequest().build();
        }
        DriverResponseDTO result = new DriverResponseDTO().mapToResponse(driver);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity createDriver(@RequestPart(value = "file") MultipartFile file, @ModelAttribute DriverRequestDTO driverRequest) {

        Driver driver = new Driver();
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
             driver = driverService.save(driverRequest, file);
            return ResponseEntity.ok().body(new DriverResponseDTO().mapToResponse(driver));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(driver);
        }
    }

    @PutMapping(value = "id/{id}")
    public ResponseEntity<DriverResponseDTO> updateDriver(@PathVariable("id") Integer id, @RequestBody DriverRequestDTO driverRequest) {
       try {
           Driver driver = driverService.update(id, driverRequest);
           return ResponseEntity.ok().body(new DriverResponseDTO().mapToResponse(driver));
       } catch (Exception ex) {
           return ResponseEntity.badRequest().build();
       }
    }

    @PutMapping(value = "update-avatar/{id}")
    public ResponseEntity updateAvatar(@PathVariable("id") Integer id, @RequestPart(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Driver driver = driverService.updateAvatar(id, file);
            return ResponseEntity.ok().body(new DriverResponseDTO().mapToResponse(driver));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "fleet-manager/{id}")
    public ResponseEntity<List<DriverResponseDTO>> getAllDriver(@PathVariable("id") Integer id) {
        List<Driver> drivers = driverService.findAllByFleetManager(id);
        if (drivers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<DriverResponseDTO> result = new ArrayList<>(new DriverResponseDTO().mapToListResponse(drivers));
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping(value = "token-device/{id}")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<String> updateTokenDevice(@PathVariable("id") Integer driverId,@RequestBody String token){
        Driver driver = driverService.findById(driverId);
        if (driver == null){
            return ResponseEntity.noContent().build();
        }else{
            driver.setTokenDevice(token.substring(1, token.length()-1));
            return ResponseEntity.ok().body(driverService.updateTokenDevice(driver));
        }
    }

    @PostMapping(value = "create-dateoff")
    public ResponseEntity createDateOff(@RequestBody DayOffRequestDTO dayOffRequestDTO){
        try {
            driverService.createDayOff(dayOffRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}


