package fmalc.api.dto;

import fmalc.api.entity.Vehicle;
import lombok.Data;
import org.modelmapper.ModelMapper;

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

    public VehicleReponseDTO convertVehicle(Vehicle vehicle){
        ModelMapper modelMapper = new ModelMapper();
        VehicleReponseDTO vehicleReponseDTO = modelMapper.map(vehicle, VehicleReponseDTO.class);
        return vehicleReponseDTO;
    }
}
