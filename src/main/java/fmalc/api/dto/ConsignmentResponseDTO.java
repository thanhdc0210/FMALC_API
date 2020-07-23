package fmalc.api.dto;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.Place;
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
    private String ownerReasonNote;
    private Double weight;
    private Integer status;
    private String statusStr;
    private List<PlaceResponeDTO> places;

    public ConsignmentResponseDTO mapToResponse(Consignment baseEntitie) {
        ModelMapper modelMapper = new ModelMapper();
        ConsignmentResponseDTO consignmentResponseDTO = modelMapper.map(baseEntitie, ConsignmentResponseDTO.class);
        consignmentResponseDTO.setStatusStr(ConsignmentStatusEnum.getValueEnumToShow(consignmentResponseDTO.getStatus()));

        List<PlaceResponeDTO> deliveryDetailResponseDTOs = new ArrayList<>();
        for (Place deliveryDetail: baseEntitie.getPlaces()) {
            PlaceResponeDTO deliveryDetailResponseDTO = modelMapper.map(deliveryDetail, PlaceResponeDTO.class);

            deliveryDetailResponseDTOs.add(deliveryDetailResponseDTO);
        }
        consignmentResponseDTO.setPlaces(deliveryDetailResponseDTOs);

        return consignmentResponseDTO;
    }

    public List<ConsignmentResponseDTO> mapToListResponse(List<Consignment> consignments) {
        return consignments.stream()
                .map(x -> mapToResponse(x))
                .collect(Collectors.toList());
    }
    public Consignment mapToEntity(ConsignmentResponseDTO baseEntitie) {
        ModelMapper modelMapper = new ModelMapper();
        Consignment consignment = modelMapper.map(baseEntitie, Consignment.class);
//        consignmentResponseDTO.setStatusStr(ConsignmentStatusEnum.getValueEnumToShow(consignmentResponseDTO.getStatus()));

        List<Place> places = new ArrayList<>();
        for (PlaceResponeDTO deliveryDetail: baseEntitie.getPlaces()) {
            Place palce = modelMapper.map(deliveryDetail, Place.class);

            places.add(palce);
        }
        consignment.setPlaces(places);

        return consignment;
    }
}
