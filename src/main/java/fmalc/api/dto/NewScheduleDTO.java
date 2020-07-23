package fmalc.api.dto;

import lombok.Data;

import java.util.*;

@Data
public class NewScheduleDTO {
    private List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS;
    private ParkingDTO parkingDTO;
}
