package fmalc.api.dto;

import fmalc.api.entity.Schedule;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScheduleDTO {
    private Integer id;
    private VehicleForDetailDTO vehicle;
    private DriverForScheduleDTO driver;
    private ConsignmentResponseDTO consignment;
    private String imageConsignment;
    private String note;
    private Boolean isApprove;
    private Collection<LocationResponeDTO> locations;

    public ScheduleDTO convertSchedule(Schedule schedule){
        ModelMapper modelMapper = new ModelMapper();
        ScheduleDTO scheduleForLocationDTO = modelMapper.map(schedule, ScheduleDTO.class);
        return  scheduleForLocationDTO;
    }
    public List<ScheduleDTO> mapToListResponse(List<Schedule> baseEntities) {
        List<ScheduleDTO> result = baseEntities
                .stream()
                .map(driver -> convertSchedule(driver))
                .collect(Collectors.toList());
        return result;
    }
}
