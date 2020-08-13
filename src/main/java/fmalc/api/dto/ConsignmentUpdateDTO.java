package fmalc.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConsignmentUpdateDTO {
    private Integer id;
    private String ownerName;
    private String ownerNote;
    private List<ScheduleUpdateConsDTO> schedules;
    private List<PlaceResponeDTO> places;
}
