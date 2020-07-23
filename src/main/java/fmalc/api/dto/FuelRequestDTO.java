package fmalc.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelRequestDTO {
    private Integer fuelTypeId;
    private Integer kmOld;
    private Double unitPriceAtFillingTime;
    private Double volume;
    private String vehicleLicensePlates;
}
