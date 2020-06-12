package fmalc.api.response;

import fmalc.api.entities.*;
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
public class DetailedConsignmentResponse {
    private Integer consignment_id;
    private String license_plates;
    private List<DeliveredPlace> deliveredPlaces;
    private List<ReceivedPlace> receivedPlaces;
    private String owner_note;

    public DetailedConsignmentResponse(Consignment consignment){

        Timestamp planned_delivered_time = null;
        String delivered_place_name = null;
        String delivered_place_address = null;
        Timestamp planned_received_time = null;
        String received_place_name = null;
        String received_place_address = null;

        if (deliveredPlaces == null){
            deliveredPlaces = new ArrayList<>();
        }

        if (receivedPlaces == null){
            receivedPlaces = new ArrayList<>();
        }

        this.consignment_id = consignment.getId();
        Collection<Schedule> schedulesList = consignment.getShedules();
        for (Schedule schedule : schedulesList){
            this.license_plates = schedule.getVehicle().getLicensePlates();
        }

        Collection<DeliveryDetail> deliveryDetailList = consignment.getDeliveries();
        for (DeliveryDetail deliveryDetail : deliveryDetailList){
            received_place_name = deliveryDetail.getReceivedPlaces().getReceived_place_name();
            received_place_address = deliveryDetail.getReceivedPlaces().getAddress();
            delivered_place_name = deliveryDetail.getDeliveredPlaces().getDelivered_place_name();
            delivered_place_address = deliveryDetail.getDeliveredPlaces().getAddress();
            planned_received_time = deliveryDetail.getReceivedPlaces().getPlannedReceiveTime();
            planned_delivered_time = deliveryDetail.getDeliveredPlaces().getPlannedDeliveryTime();
            ReceivedPlace receivedPlace = new ReceivedPlace(planned_received_time, received_place_name, received_place_address);
            DeliveredPlace deliveredPlace = new DeliveredPlace(planned_delivered_time, delivered_place_name, delivered_place_address);
            receivedPlaces.add(receivedPlace);
            deliveredPlaces.add(deliveredPlace);
        }
        if (consignment.getOwnerNote() != null){
            this.owner_note = consignment.getOwnerNote();
        }else{
            this.owner_note = "";
        }
    }

    public List<DetailedConsignmentResponse> mapToListResponse(List<Consignment> baseEntities) {
        return baseEntities
                .stream()
                .map(DetailedConsignmentResponse::new)
                .collect(Collectors.toList());
    }
}
