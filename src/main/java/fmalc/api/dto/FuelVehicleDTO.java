package fmalc.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fmalc.api.entity.Fuel;
import fmalc.api.entity.FuelType;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FuelVehicleDTO {
    private Integer id;
    private Double unitPriceAtFillingTime;
    private Double volume;
    private Integer kmOld;
    private Date fillingDate;
    private FuelTypeVehicleDTO fuelType;

    public FuelVehicleDTO convertDTO(Fuel fuel){
        ModelMapper modelMapper = new ModelMapper();
        FuelVehicleDTO fuelVehicleDTO = modelMapper.map(fuel, FuelVehicleDTO.class);

        return fuelVehicleDTO;
    }


    public List<FuelVehicleDTO> mapToListResponse(List<Fuel> fuels) {
        return fuels.stream()
                .map(x -> convertDTO(x))
                .collect(Collectors.toList());
    }
}
