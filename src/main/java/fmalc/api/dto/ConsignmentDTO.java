package fmalc.api.dto;

import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentDTO {

    private Integer consignmentId;
    private String ownerName;
    private List<PlaceResponeDTO> places;
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
        Schedule schedule = consignment.getSchedule();
        this.licensePlates = schedule.getVehicle().getLicensePlates();
        this.driverName = schedule.getDriver().getName();
        this.weight = consignment.getWeight();
        this.status = ConsignmentStatusEnum.getValueEnumToShow(consignment.getStatus());
        for(Place place : consignment.getPlaces()){
            places.add(new PlaceResponeDTO().convertPlace(place));
        }

        this.status = ConsignmentStatusEnum.getValueEnumToShow(consignment.getStatus());
    }

    public List<ConsignmentDTO> mapToListResponse(List<Consignment> baseEntities){
        return baseEntities.stream().map(ConsignmentDTO::new).collect(Collectors.toList());
    }
}
