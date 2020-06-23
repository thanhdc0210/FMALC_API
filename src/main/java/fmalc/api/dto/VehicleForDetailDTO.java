package fmalc.api.dto;

import lombok.Data;



@Data
public class VehicleForDetailDTO {

    private int status;
    private String vehicleName;
    private String dateOfManufacture;
    private Integer id;
    private Integer kilometerRunning;
    private String licensePlates;
    private double weight;
    private VehicleTypeForVehicleDTO vehicleType;


}
