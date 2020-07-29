package fmalc.api.dto;

import lombok.Data;

@Data
public class StatusToUpdateDTO {
    private int driver_status;
    private int vehicle_status;
    private int consignment_status;
}
