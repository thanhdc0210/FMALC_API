package fmalc.api.controller;

import fmalc.api.entities.Driver;
import fmalc.api.repository.DriverRepository;
import fmalc.api.request.DriverRequest;
import fmalc.api.response.DriverResponse;
import fmalc.api.service.DriverService;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1.0/drivers")
public class DriverController {
    @Autowired
    DriverService driverService;

    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAllDriver() {
        List<Driver> drivers = driverService.findAll();
        if (drivers.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<DriverResponse> result = new ArrayList<>(new DriverResponse().mapToListResponse(drivers));
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<DriverResponse> findById(@PathVariable("id") Integer id) {
        Driver driver = driverService.findById(id);
        if (driver == null) {
            return ResponseEntity.badRequest().build();
        }
        DriverResponse result = new DriverResponse().mapToResponse(driver);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity createDriver(@RequestBody DriverRequest driverRequest) {
        try {
            Driver driver = driverService.save(driverRequest);
            return ResponseEntity.ok().body(new DriverResponse().mapToResponse(driver));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
