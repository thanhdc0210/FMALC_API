package fmalc.api.dto;

import lombok.*;

import java.util.List;

@Data
public class ConsignmentRequestDTO {
    private Integer amount;
    private String ownerName;
    private String ownerNote;
    private Double weight;
    private Integer status;
    private List<PlaceRequestDTO> place;
}
