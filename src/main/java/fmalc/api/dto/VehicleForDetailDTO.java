package fmalc.api.dto;

import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;


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
    private double averageFuel;
    private double maximumCapacity;

    public VehicleForDetailDTO convertToDto(Vehicle vehicle) {
        ModelMapper modelMapper = new ModelMapper();
        VehicleForDetailDTO dto = modelMapper.map(vehicle, VehicleForDetailDTO.class);

        return dto;
    }

    public Vehicle convertToEnity(VehicleForDetailDTO vehicle) {
        ModelMapper modelMapper = new ModelMapper();
        Vehicle dto = modelMapper.map(vehicle, Vehicle.class);

        return dto;
    }

    public List<VehicleForDetailDTO> mapToListResponse(List<Vehicle> baseEntities) {
        List<VehicleForDetailDTO> result = baseEntities
                .stream()
                .map(vehicle -> convertToDto(vehicle))
                .collect(Collectors.toList());
        return result;
    }

}
