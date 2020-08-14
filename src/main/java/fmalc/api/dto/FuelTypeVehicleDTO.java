package fmalc.api.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class FuelTypeVehicleDTO {
    private Integer id;
    private Double currentPrice;
    private String type;
}
