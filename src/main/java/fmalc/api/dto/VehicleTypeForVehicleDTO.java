package fmalc.api.dto;

import lombok.Data;

@Data
public class VehicleTypeForVehicleDTO {
        private  int id;

    private double weight;
    private DriverLicenseRequestDTO driver_license;
}
