package fmalc.api.dto;

import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.TypeLocationEnum;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetailedConsignmentDTO {
    private Integer consignmentId;
    private List<PlaceDTO> places;
    private String licensePlates; // Biển số xe
    private String status;
    private String ownerNote;

    public DetailedConsignmentDTO(Consignment consignment) {


        if (places == null){
            places = new ArrayList<>();
        }

        this.consignmentId = consignment.getId();
        this.ownerNote = consignment.getOwnerNote();
//        Collection<DeliveryDetail> deliveryDetailList = consignment.getDeliveries();
//        for (DeliveryDetail deliveryDetail : deliveryDetailList){
//            PlaceDTO placeDTO = new PlaceDTO();
//            placeDTO.setPriority(deliveryDetail.getPriority());
//            placeDTO.setPlannedTime(deliveryDetail.getPlace().getPlannedTime());
//            placeDTO.setName(deliveryDetail.getPlace().getName());
//            placeDTO.setType(TypeLocationEnum.getValueEnumToShow(deliveryDetail.getPlace().getType()));
//            placeDTO.setAddress(deliveryDetail.getPlace().getAddress());
//            placeDTO.setLongitude(deliveryDetail.getPlace().getLongitude());
//            placeDTO.setLatitude(deliveryDetail.getPlace().getLatitude());
//            places.add(placeDTO);
//        }
        licensePlates = consignment.getSchedule().getVehicle().getLicensePlates();
        this.status = ConsignmentStatusEnum.getValueEnumToShow(consignment.getStatus());
    }
}
