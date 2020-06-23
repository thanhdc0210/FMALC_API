package fmalc.api.dto;

import lombok.Data;

@Data
public class VehicleTypeForVehicleDTO {
    private  int id;
    private double averageFuel;
    private double maximumCapacity;
    private String vehicleTypeName;
    private DriverLicenseRequestDTO driver_license;
}
