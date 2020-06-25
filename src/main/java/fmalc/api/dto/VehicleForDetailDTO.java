package fmalc.api.dto;

import lombok.Data;


@Data
public class VehicleForDetailDTO {

    private int status;
    private String vehicleName;
    private String dateOfManufacture;
    private Integer id;
    private int driverLicense;
    private Integer kilometerRunning;
    private String licensePlates;
    private double weight;
    private int vehicleType;
    private double averageFuel;
    private double maximumCapacity;

}
