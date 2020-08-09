package fmalc.api.dto;

import fmalc.api.entity.FuelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelTypeResponseDTO {

    private String vehicleLicensePlate;
    private List<FuelType> fuelTypeList;

}
