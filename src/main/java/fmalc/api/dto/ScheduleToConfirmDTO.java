package fmalc.api.dto;

import fmalc.api.entity.Driver;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class ScheduleToConfirmDTO {
    private List<VehicleForDetailDTO> vehicleForDetailDTOS;
    private List<DriverForScheduleDTO> driverForScheduleDTOS;
    private List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS;

    public ScheduleForConsignmentDTO convertSchedule(Schedule schedule){
        ModelMapper modelMapper = new ModelMapper();
        ScheduleForConsignmentDTO scheduleForLocationDTO = modelMapper.map(schedule, ScheduleForConsignmentDTO.class);
        return  scheduleForLocationDTO;
    }
    public List<ScheduleForConsignmentDTO> mapToListResponse(List<Schedule> baseEntities) {
        List<ScheduleForConsignmentDTO> result = baseEntities
                .stream()
                .map(driver -> convertSchedule(driver))
                .collect(Collectors.toList());
        return result;
    }
}
