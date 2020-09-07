package fmalc.api.dto;

import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class VehicleResponseDTO implements Serializable {
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

    public VehicleResponseDTO convertVehicle(Vehicle vehicle){
        ModelMapper modelMapper = new ModelMapper();
        VehicleResponseDTO vehicleResponseDTO = modelMapper.map(vehicle, VehicleResponseDTO.class);
        return vehicleResponseDTO;
    }

    public List<VehicleResponseDTO> mapToListResponse(List<Vehicle> baseEntities){
        List<VehicleResponseDTO> result = baseEntities
                .stream()
                .map(vehicle -> convertVehicle(vehicle))
                .collect(Collectors.toList());
        return result;
    }
}
