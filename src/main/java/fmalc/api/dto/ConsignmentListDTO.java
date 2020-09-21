package fmalc.api.dto;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.Place;
import fmalc.api.enums.ConsignmentStatusEnum;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Data
public class ConsignmentListDTO {
    private Integer id;
    private Integer amount;
    private String ownerName;
    private String ownerNote;
    private String ownerReasonNote;
    private Double weight;
    private Integer status;
    private String statusStr;
    private List<PlaceResponeDTO> places;
    private List<VehicleForDetailDTO> vehicles;
    private List<DriverResponseDTO> drivers;


    public ConsignmentListDTO mapToResponse(Consignment baseEntitie) {
        ModelMapper modelMapper = new ModelMapper();
        ConsignmentListDTO consignmentResponseDTO = modelMapper.map(baseEntitie, ConsignmentListDTO.class);
        consignmentResponseDTO.setStatusStr(ConsignmentStatusEnum.getValueEnumToShow(consignmentResponseDTO.getStatus()));
        List<PlaceResponseDTO> deliveryDetailResponseDTOs = new ArrayList<>();
        for (Place deliveryDetail: baseEntitie.getPlaces()) {
            PlaceResponseDTO deliveryDetailResponseDTO = modelMapper.map(deliveryDetail, PlaceResponseDTO.class);

            deliveryDetailResponseDTOs.add(deliveryDetailResponseDTO);
        }

        return consignmentResponseDTO;
    }

    public List<ConsignmentListDTO> mapToListResponse(List<Consignment> consignments) {
        return consignments.stream()
                .map(x -> mapToResponse(x))
                .collect(Collectors.toList());
    }
}
