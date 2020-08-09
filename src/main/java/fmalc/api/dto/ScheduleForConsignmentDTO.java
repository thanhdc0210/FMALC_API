package fmalc.api.dto;

import fmalc.api.entity.Schedule;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScheduleForConsignmentDTO {
    private Integer id;
    private VehicleForDetailDTO vehicle;
    private DriverForScheduleDTO driver;
    private boolean isApprove;
    private ConsignmentResponseDTO consignment;
    private String imageConsignment;
    private String note;
    private DriverForScheduleDTO inheritance;


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
