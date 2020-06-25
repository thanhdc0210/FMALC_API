package fmalc.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class VehicleReponseDTO implements Serializable {
    private int status;
    private String vehicleName;
    private Date dateOfManufacture;
    private Integer id;
    private int driverLicense;
    private Integer kilometerRunning;
    private double maximumCapacity;
    private double averageFuel;
    private String licensePlates;
    private double weight;
}
