package fmalc.api.dto;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.Schedule;
import fmalc.api.enums.ConsignmentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDTO {
    private Integer scheduleId;
    private String ownerName;
    private List<PlaceResponeDTO> places;
    private String licensePlates; // Biển số xe
    private String driverName;
    private Double weight; // Khối lượng lô hàng
    private String status;
    private Boolean isInheritance;

    public ScheduleResponseDTO(Schedule schedule){
        if (places == null){
            places = new ArrayList<>();
        }

        this.scheduleId = schedule.getId();
        this.ownerName = schedule.getConsignment().getOwnerName();
        this.licensePlates = schedule.getVehicle().getLicensePlates();
        this.driverName = schedule.getDriver().getName();
        this.weight = schedule.getConsignment().getWeight();
        this.status = ConsignmentStatusEnum.getValueEnumToShow(schedule.getConsignment().getStatus());
        this.places = new PlaceResponeDTO().mapToListResponse(List.copyOf(schedule.getConsignment().getPlaces()));

        this.status = ConsignmentStatusEnum.getValueEnumToShow(schedule.getConsignment().getStatus());
        if (schedule.getInheritance()!= null){
            this.isInheritance = true;
        } else {
            this.isInheritance = false;
        }
    }

    public List<ScheduleResponseDTO> mapToListResponse(List<Schedule> baseEntities){
        return baseEntities.stream().map(ScheduleResponseDTO::new).collect(Collectors.toList());
    }
}
