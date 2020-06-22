package fmalc.api.dto;

import lombok.Data;

@Data
public class DeliveryDetailResponseDTO {
    private Integer id;
    private PlaceResponseDTO place;
    private Integer priority;
}
