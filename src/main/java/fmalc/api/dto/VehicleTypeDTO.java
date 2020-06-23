package fmalc.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleTypeDTO implements Serializable {
    private  int id;
    
    private double averageFuel;
    private double maximumCapacity;
    private String vehicleTypeName;
    private DriverLicenseRequestDTO driver_license;
}
