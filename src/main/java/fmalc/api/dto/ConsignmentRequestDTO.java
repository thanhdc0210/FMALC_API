package fmalc.api.dto;

import lombok.*;

import java.util.List;

@Data
public class ConsignmentRequestDTO {
    private Integer amount;
    private String ownerName;
    private String ownerNote;
    private String ownerReasonNote;
    private Double weight;
    private Integer status;
    private String imageConsignment;
    private List<VehicleConsignmentDTO> vehicles;
    private List<PlaceRequestDTO> place;
}
