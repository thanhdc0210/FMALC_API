package fmalc.api.dto;

import fmalc.api.entity.Place;
import fmalc.api.entity.Schedule;
import fmalc.api.enums.ConsignmentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer kilometer_running;

    public DetailedScheduleDTO(Schedule schedule) {


        if (places == null){
            places = new ArrayList<>();
        }

        this.consignmentId = schedule.getConsignment().getId();
        this.scheduleId = schedule.getId();
        this.ownerNote = schedule.getConsignment().getOwnerNote();
        this.kilometer_running = schedule.getVehicle().getKilometerRunning();
        for(Place place : schedule.getConsignment().getPlaces()){
            places.add(new PlaceResponeDTO().convertPlace(place));
        }

        licensePlates = schedule.getVehicle().getLicensePlates();
        this.status = ConsignmentStatusEnum.getValueEnumToShow(schedule.getConsignment().getStatus());
    }
}
