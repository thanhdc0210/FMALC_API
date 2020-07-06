<<<<<<< HEAD
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
    private String ownerReasonNote;
    private Double weight;
    private Integer status;
    private String statusStr;
    private List<PlaceResponseDTO> places;

    public ConsignmentResponseDTO mapToResponse(Consignment baseEntitie) {
        ModelMapper modelMapper = new ModelMapper();
        ConsignmentResponseDTO consignmentResponseDTO = modelMapper.map(baseEntitie, ConsignmentResponseDTO.class);
        consignmentResponseDTO.setStatusStr(ConsignmentStatusEnum.getValueEnumToShow(consignmentResponseDTO.getStatus()));
=======
//package fmalc.api.dto;
//
//import fmalc.api.entity.Consignment;
//import fmalc.api.enums.ConsignmentStatusEnum;
//import lombok.Data;
//import org.modelmapper.ModelMapper;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Data
//public class ConsignmentResponseDTO {
//    private Integer id;
//    private Integer amount;
//    private String ownerName;
//    private String ownerNote;
//    private String ownerReasonNote;
//    private Double weight;
//    private Integer status;
//    private String statusStr;
//    private List<DeliveryDetailResponseDTO> deliveryDetailResponseDTOs;
//
//    public ConsignmentResponseDTO mapToResponse(Consignment baseEntitie) {
//        ModelMapper modelMapper = new ModelMapper();
//        ConsignmentResponseDTO consignmentResponseDTO = modelMapper.map(baseEntitie, ConsignmentResponseDTO.class);
//        consignmentResponseDTO.setStatusStr(ConsignmentStatusEnum.getValueEnumToShow(consignmentResponseDTO.getStatus()));
>>>>>>> 0c51869f5ed2f4607a8bffe3c5deb6b1b955a585
//        List<DeliveryDetailResponseDTO> deliveryDetailResponseDTOs = new ArrayList<>();
//        for (DeliveryDetail deliveryDetail: baseEntitie.getDeliveries()) {
//            DeliveryDetailResponseDTO deliveryDetailResponseDTO = modelMapper.map(deliveryDetail, DeliveryDetailResponseDTO.class);
//
//            deliveryDetailResponseDTOs.add(deliveryDetailResponseDTO);
//        }
//        consignmentResponseDTO.setDeliveryDetailResponseDTOs(deliveryDetailResponseDTOs);
<<<<<<< HEAD

        return consignmentResponseDTO;
    }

    public List<ConsignmentResponseDTO> mapToListResponse(List<Consignment> consignments) {
        return consignments.stream()
                .map(x -> mapToResponse(x))
                .collect(Collectors.toList());
    }
}
=======
//        return consignmentResponseDTO;
//    }
//
//    public List<ConsignmentResponseDTO> mapToListResponse(List<Consignment> consignments) {
//        return consignments.stream()
//                .map(x -> mapToResponse(x))
//                .collect(Collectors.toList());
//    }
//}
>>>>>>> 0c51869f5ed2f4607a8bffe3c5deb6b1b955a585
