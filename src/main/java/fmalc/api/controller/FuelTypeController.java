package fmalc.api.controller;

import fmalc.api.dto.FuelTypeResponseDTO;
import fmalc.api.entity.FuelType;
import fmalc.api.entity.Vehicle;
import fmalc.api.service.FuelTypeService;
import fmalc.api.service.VehicleService;
import fmalc.api.util.FuelTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/fuel-types")
public class FuelTypeController {

    @Autowired
    FuelTypeService fuelTypeService;
    @Autowired
    VehicleService vehicleService;

    @Autowired
    FuelTypeUtil fuelTypeUtil;

    @GetMapping("/fuel-type")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<FuelTypeResponseDTO> getFuelTypesAndVehicleLicensePlate(@RequestParam("status") List<Integer> status,
                                                                                  @RequestParam("username") String username) {
        try {
            List<FuelType> fuelTypes = fuelTypeService.getListFuelType();
            String licensePlate = vehicleService.findLicensePlatesForMakingReportBeforeRunningOrWhileRunning(status, username);
            Vehicle vehicle = vehicleService.findVehicleByLicensePlates(licensePlate);
            if (fuelTypes == null) {
                return ResponseEntity.noContent().build();
            } else {
                if (licensePlate == null) {
                    FuelTypeResponseDTO fuelTypeResponseDTO = new FuelTypeResponseDTO();
                    fuelTypeResponseDTO.setVehicleLicensePlate(null);
                    fuelTypeResponseDTO.setFuelTypeList(fuelTypes);
                    return ResponseEntity.ok().body(fuelTypeResponseDTO);
                } else {
                    FuelTypeResponseDTO fuelTypeResponseDTO = new FuelTypeResponseDTO();
                    fuelTypeResponseDTO.setVehicleLicensePlate(licensePlate);
                    fuelTypeResponseDTO.setCapacity(vehicle.getMaximumCapacity());
                    fuelTypeResponseDTO.setFuelTypeList(fuelTypes);
                    return ResponseEntity.ok().body(fuelTypeResponseDTO);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FuelType>> getPriceFuel() {
        List<FuelType> fuelTypes = fuelTypeService.getListFuelType();
        if (fuelTypes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        fuelTypes.stream().forEach(x -> x.setFuelHistories(null));
        return ResponseEntity.ok().body(fuelTypes);
    }

    @GetMapping("/refresh")
    public ResponseEntity refreshPrice() {
        fuelTypeUtil.run();
        return ResponseEntity.noContent().build();
    }
}
