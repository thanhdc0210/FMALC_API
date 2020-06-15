package fmalc.api.dto;

import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConsignmentDTO {

    private Integer consignmentId;
    private String ownerName;
    private ReceivedPlace receivedPlace = new ReceivedPlace();
    private List<DeliveredPlace> deliveredPlaces;
    private String licensePlates; // Biển số xe
    private String driverName;
    private Double weight; // Khối lượng lô hàng
    private String status;

    public ConsignmentDTO(Consignment consignment) {

        Timestamp planned_delivered_time = null;
        String delivered_place_name = null;
        Timestamp planned_received_time = null;
        String received_place_name = null;

        if (deliveredPlaces == null){
            deliveredPlaces = new ArrayList<>();
        }

        this.consignmentId = consignment.getId();
        this.ownerName = consignment.getOwnerName();
        Collection<DeliveryDetail> deliveryDetailList = consignment.getDeliveries();
        for (DeliveryDetail deliveryDetail : deliveryDetailList){
            received_place_name = deliveryDetail.getReceivedPlaces().getReceivedPlaceName();
            delivered_place_name = deliveryDetail.getDeliveredPlaces().getDeliveredPlaceName();
            planned_received_time = deliveryDetail.getReceivedPlaces().getPlannedReceiveTime();
            planned_delivered_time = deliveryDetail.getDeliveredPlaces().getPlannedDeliveryTime();
            receivedPlace.setPlannedReceiveTime(planned_received_time);
            receivedPlace.setReceivedPlaceName(received_place_name);
            DeliveredPlace deliveredPlace = new DeliveredPlace(planned_delivered_time, delivered_place_name);
            deliveredPlaces.add(deliveredPlace);
        }

        Collection<Schedule> schedulesList = consignment.getShedules();
        for (Schedule schedule : schedulesList){
            this.licensePlates = schedule.getVehicle().getLicensePlates();
            this.driverName = schedule.getDriver().getName();
        }
        this.weight = consignment.getWeight();
        this.status = ConsignmentStatusEnum.ĐANG_CHỜ_XỬ_LÝ.getValueEnumToShow(consignment.getStatus());
    }

    public List<ConsignmentDTO> mapToListResponse(List<Consignment> baseEntities) {
        return baseEntities
                .stream()
                .map(ConsignmentDTO::new)
                .collect(Collectors.toList());
    }
}
