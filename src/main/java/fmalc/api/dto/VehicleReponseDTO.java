package fmalc.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleReponseDTO implements Serializable {
    private int status;
    private String vehicleName;
    private String dateOfManufacture;
    private Integer id;
    private Integer kilometerRunning;
    private String licensePlates;
    private double weight;
    private VehicleTypeDTO vehicleType;

}
