package fmalc.api.dto;

import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.DriverStatusEnum;
import fmalc.api.enums.TypeLocationEnum;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentDTO {

    private Integer consignmentId;
    private String ownerName;
    private List<PlaceDTO> places;
    private String licensePlates; // Biển số xe
    private String driverName;
    private Double weight; // Khối lượng lô hàng
    private String status;

    public ConsignmentDTO(Consignment consignment) {


        if (places == null){
            places = new ArrayList<>();
        }

        this.consignmentId = consignment.getId();
        this.ownerName = consignment.getOwnerName();
//        Collection<DeliveryDetail> deliveryDetailList = consignment.getDeliveries();
//        for (DeliveryDetail deliveryDetail : deliveryDetailList){
//            PlaceDTO placeDTO = new PlaceDTO();
//            placeDTO.setPriority(deliveryDetail.getPriority());
//            placeDTO.setPlannedTime(deliveryDetail.getPlace().getPlannedTime());
//            placeDTO.setName(deliveryDetail.getPlace().getName());
//            placeDTO.setType(TypeLocationEnum.getValueEnumToShow(deliveryDetail.getPlace().getType()));
//            placeDTO.setActualTime(deliveryDetail.getPlace().getActualTime());
//            places.add(placeDTO);
//        }

        Schedule schedule = consignment.getSchedule();
        this.licensePlates = schedule.getVehicle().getLicensePlates();
        this.driverName = schedule.getDriver().getName();
        this.weight = consignment.getWeight();

        this.status = ConsignmentStatusEnum.getValueEnumToShow(consignment.getStatus());

    }

    public List<ConsignmentDTO> mapToListResponse(List<Consignment> baseEntities){
        return baseEntities.stream().map(ConsignmentDTO::new).collect(Collectors.toList());
    }
}
