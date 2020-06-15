package fmalc.api.dto;

import fmalc.api.entity.*;
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
public class DetailedConsignmentDTO {
    private Integer consignmentId;
    private String licensePlates;
    private List<DeliveredPlace> deliveredPlaces;
    private List<ReceivedPlace> receivedPlaces;
    private String ownerNote;

    public DetailedConsignmentDTO(Consignment consignment){

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

        this.consignmentId = consignment.getId();
        Collection<Schedule> schedulesList = consignment.getShedules();
        for (Schedule schedule : schedulesList){
            this.licensePlates = schedule.getVehicle().getLicensePlates();
        }

        Collection<DeliveryDetail> deliveryDetailList = consignment.getDeliveries();
        for (DeliveryDetail deliveryDetail : deliveryDetailList){
            received_place_name = deliveryDetail.getReceivedPlaces().getReceivedPlaceName();
            received_place_address = deliveryDetail.getReceivedPlaces().getAddress();
            delivered_place_name = deliveryDetail.getDeliveredPlaces().getDeliveredPlaceName();
            delivered_place_address = deliveryDetail.getDeliveredPlaces().getAddress();
            planned_received_time = deliveryDetail.getReceivedPlaces().getPlannedReceiveTime();
            planned_delivered_time = deliveryDetail.getDeliveredPlaces().getPlannedDeliveryTime();
            ReceivedPlace receivedPlace = new ReceivedPlace(planned_received_time, received_place_name, received_place_address);
            DeliveredPlace deliveredPlace = new DeliveredPlace(planned_delivered_time, delivered_place_name, delivered_place_address);
            receivedPlaces.add(receivedPlace);
            deliveredPlaces.add(deliveredPlace);
        }
        if (consignment.getOwnerNote() != null){
            this.ownerNote = consignment.getOwnerNote();
        }else{
            this.ownerNote = "";
        }
    }

    public List<DetailedConsignmentDTO> mapToListResponse(List<Consignment> baseEntities) {
        return baseEntities
                .stream()
                .map(DetailedConsignmentDTO::new)
                .collect(Collectors.toList());
    }
}
