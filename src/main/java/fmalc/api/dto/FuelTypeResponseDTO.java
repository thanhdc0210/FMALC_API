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
@AllArgsConstructor
public class FuelTypeResponseDTO {

    private String vehicleLicensePlate;
    private List<FuelType> fuelTypeList;

}
