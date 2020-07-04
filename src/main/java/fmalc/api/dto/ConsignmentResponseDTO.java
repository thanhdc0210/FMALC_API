package fmalc.api.dto;

import fmalc.api.entity.Consignment;
import fmalc.api.enums.ConsignmentStatusEnum;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ConsignmentResponseDTO {
    private Integer id;
    private Integer amount;
    private String ownerName;
    private String ownerNote;
    private Double weight;
    private Integer status;
    private String statusStr;
    private List<PlaceResponseDTO> deliveryDetailResponseDTOs;

    public ConsignmentResponseDTO mapToResponse(Consignment baseEntitie) {
        ModelMapper modelMapper = new ModelMapper();
        ConsignmentResponseDTO consignmentResponseDTO = modelMapper.map(baseEntitie, ConsignmentResponseDTO.class);
        consignmentResponseDTO.setStatusStr(ConsignmentStatusEnum.getValueEnumToShow(consignmentResponseDTO.getStatus()));
        return consignmentResponseDTO;
    }

    public List<ConsignmentResponseDTO> mapToListResponse(List<Consignment> consignments) {
        return consignments.stream()
                .map(x -> mapToResponse(x))
                .collect(Collectors.toList());
    }
}
