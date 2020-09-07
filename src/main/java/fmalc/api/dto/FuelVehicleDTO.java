package fmalc.api.dto;

import fmalc.api.entity.FuelHistory;
import lombok.Data;
import org.modelmapper.ModelMapper;

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

    public FuelVehicleDTO convertDTO(FuelHistory fuelHistory){
        ModelMapper modelMapper = new ModelMapper();
        FuelVehicleDTO fuelVehicleDTO = modelMapper.map(fuelHistory, FuelVehicleDTO.class);

        return fuelVehicleDTO;
    }


    public List<FuelVehicleDTO> mapToListResponse(List<FuelHistory> fuelHistories) {
        return fuelHistories.stream()
                .map(x -> convertDTO(x))
                .collect(Collectors.toList());
    }
}
