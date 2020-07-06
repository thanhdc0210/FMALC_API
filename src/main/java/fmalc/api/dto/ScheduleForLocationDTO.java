
package fmalc.api.dto;

import fmalc.api.entity.*;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScheduleForLocationDTO {
    private Integer id;
    private int vehicle_id;
    private int driver_id;
    private ConsignmentResponseDTO consignment;
    private String imageConsignment;
    private String note;

    public ScheduleForLocationDTO convertSchedule(Schedule schedule){
        ModelMapper modelMapper = new ModelMapper();
        ScheduleForLocationDTO scheduleForLocationDTO = modelMapper.map(schedule, ScheduleForLocationDTO.class);
        return  scheduleForLocationDTO;
    }
    public List<ScheduleForLocationDTO> mapToListResponse(List<Schedule> baseEntities) {
        List<ScheduleForLocationDTO> result = baseEntities
                .stream()
                .map(driver -> convertSchedule(driver))
                .collect(Collectors.toList());
        return result;
    }
}

