package fmalc.api.dto;

import lombok.Data;

@Data
public class VehicleForNewDTO {

    private String vehicleName;
    private String dateOfManufacture;
    private int driverLicense;
    private String licensePlates;
    private double weight;
    private double averageFuel;
    private double maximumCapacity;
}
