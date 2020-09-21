package fmalc.api.dto;

import fmalc.api.entity.Consignment;
import lombok.Data;
import org.modelmapper.ModelMapper;

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
    private List<PlaceRequestDTO> place;
    private List<VehicleConsignmentDTO> vehicles;

    public Consignment convertEntity(ConsignmentRequestDTO consignmentRequestDTO){
        ModelMapper modelMapper = new ModelMapper();
        Consignment consignment = modelMapper.map(consignmentRequestDTO, Consignment.class);
        return consignment;
    }
//    public List<>
}
