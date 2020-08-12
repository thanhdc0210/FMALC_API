package fmalc.api.dto;

import fmalc.api.entity.Place;
import fmalc.api.entity.Schedule;
import fmalc.api.enums.ConsignmentStatusEnum;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailedScheduleDTO {
    private Integer scheduleId;
    private List<PlaceResponeDTO> places;
    private String licensePlates; // Biển số xe
    private String status;
    private String ownerNote;
    private Integer consignmentId;

    public DetailedScheduleDTO(Schedule schedule) {


        if (places == null){
            places = new ArrayList<>();
        }

        this.consignmentId = schedule.getConsignment().getId();
        this.scheduleId = schedule.getId();
        this.ownerNote = schedule.getConsignment().getOwnerNote();
        for(Place place : schedule.getConsignment().getPlaces()){
            places.add(new PlaceResponeDTO().convertPlace(place));
        }

        licensePlates = schedule.getVehicle().getLicensePlates();
        this.status = ConsignmentStatusEnum.getValueEnumToShow(schedule.getConsignment().getStatus());
    }
}
