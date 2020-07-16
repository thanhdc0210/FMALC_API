package fmalc.api.dto;

import fmalc.api.entity.Account;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.FuelType;
import fmalc.api.entity.Place;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Data
@AllArgsConstructor
public class FuelTypeResponseDTO {

    private Integer id;
    private Double price ;
    private String type;



    public List<FuelTypeResponseDTO> mapToListResponse(List<FuelType> fuelTypes) {
        return fuelTypes.stream()
                .map(x -> mapToResponse(x))
                .collect(Collectors.toList());
    }

    public FuelTypeResponseDTO mapToResponse(FuelType fuelType) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(fuelType, FuelTypeResponseDTO.class);
    }
}
