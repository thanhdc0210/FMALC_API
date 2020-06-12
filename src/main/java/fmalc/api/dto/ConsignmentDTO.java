package fmalc.api.dto;

import fmalc.api.entities.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Integer consignment_id;
    private String owner_name;
    private List<DeliveredPlace> deliveredPlaces;
    private List<ReceivedPlace> receivedPlaces;
    private String license_plates; // Biển số xe
    private String driver_name;
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

        if (receivedPlaces == null){
            receivedPlaces = new ArrayList<>();
        }

        this.consignment_id = consignment.getId();
        this.owner_name = consignment.getOwnerName();
        Collection<DeliveryDetail> deliveryDetailList = consignment.getDeliveries();
        for (DeliveryDetail deliveryDetail : deliveryDetailList){
            received_place_name = deliveryDetail.getReceivedPlaces().getReceived_place_name();
            delivered_place_name = deliveryDetail.getDeliveredPlaces().getDelivered_place_name();
            planned_received_time = deliveryDetail.getReceivedPlaces().getPlannedReceiveTime();
            planned_delivered_time = deliveryDetail.getDeliveredPlaces().getPlannedDeliveryTime();
            ReceivedPlace receivedPlace = new ReceivedPlace(planned_received_time, received_place_name);
            DeliveredPlace deliveredPlace = new DeliveredPlace(planned_delivered_time, delivered_place_name);
            receivedPlaces.add(receivedPlace);
            deliveredPlaces.add(deliveredPlace);
        }

        Collection<Schedule> schedulesList = consignment.getShedules();
        for (Schedule schedule : schedulesList){
            this.license_plates = schedule.getVehicle().getLicensePlates();
            this.driver_name = schedule.getDriver().getName();
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
