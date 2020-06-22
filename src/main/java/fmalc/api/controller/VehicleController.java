package fmalc.api.controller;

import fmalc.api.dto.VehicleLicensePlateDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/vehicles")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @GetMapping(value = "/report-inspection")
    public ResponseEntity<List<String>> findVehicleLicensePlatesForReportInspection
            (@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username){

        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        System.out.println(currentDate);

        List<String> vehiclePlates = vehicleService.findVehicleLicensePlatesForReportInspection(status, username, currentDate);

        if (vehiclePlates == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(vehiclePlates);
    }
}
