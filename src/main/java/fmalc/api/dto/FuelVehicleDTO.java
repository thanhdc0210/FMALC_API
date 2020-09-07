package fmalc.api.dto;

import fmalc.api.entity.RefuelHistory;
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

    public FuelVehicleDTO convertDTO(RefuelHistory refuelHistory){
        ModelMapper modelMapper = new ModelMapper();
        FuelVehicleDTO fuelVehicleDTO = modelMapper.map(refuelHistory, FuelVehicleDTO.class);

        return fuelVehicleDTO;
    }


    public List<FuelVehicleDTO> mapToListResponse(List<RefuelHistory> fuelHistories) {
        return fuelHistories.stream()
                .map(x -> convertDTO(x))
                .collect(Collectors.toList());
    }
}
