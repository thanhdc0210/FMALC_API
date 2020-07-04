//package fmalc.api.dto;
//
//
//import fmalc.api.entity.Schedule;
//import lombok.Data;
//import org.modelmapper.ModelMapper;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Data
//public class DeliveryDetailResponseDTO {
//    private Integer id;
//    private PlaceResponseDTO place;
//    private Integer priority;
//    private int consignment;
//
//    public DeliveryDetailResponseDTO convertDelivery(DeliveryDetail deliveryDetail){
//        ModelMapper modelMapper = new ModelMapper();
//        DeliveryDetailResponseDTO deliveryDetailResponseDTO = modelMapper.map(deliveryDetail,DeliveryDetailResponseDTO.class);
//        return deliveryDetailResponseDTO;
//    }
//    public List<DeliveryDetailResponseDTO> mapToListResponse(List<DeliveryDetail> baseEntities) {
//        List<DeliveryDetailResponseDTO> result = baseEntities
//                .stream()
//                .map(driver -> convertDelivery(driver))
//                .collect(Collectors.toList());
//        return result;
//    }
//}
