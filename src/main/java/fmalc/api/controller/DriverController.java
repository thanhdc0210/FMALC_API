
package fmalc.api.controller;

import fmalc.api.dto.DriverRequestDTO;
import fmalc.api.dto.DriverResponseDTO;
import fmalc.api.entity.Driver;
import fmalc.api.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/drivers")
public class DriverController {

    @Autowired
    DriverService driverService;

    @GetMapping
    public ResponseEntity<List<DriverResponseDTO>> getAllDriver(@RequestParam(value = "searchPhone", defaultValue = "") String searchPhone) {
        List<Driver> drivers = driverService.findAllAndSearch(searchPhone);
        if (drivers.isEmpty()) {
            return ResponseEntity.noContent().build();
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

}


