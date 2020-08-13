package fmalc.api.dto;

import fmalc.api.entity.Schedule;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScheduleForDetailDTO {
    private Integer id;
    private VehicleForDetailDTO vehicle;
    private DriverForDetailDTO driver;
    private boolean isApprove;
    private String imageConsignment;
    private String note;
    private DriverForDetailDTO inheritance;

    public ScheduleForDetailDTO mapToResponse(Schedule schedule){
        ModelMapper modelMapper = new ModelMapper();
        ScheduleForDetailDTO scheduleForDetailDTO = modelMapper.map(schedule, ScheduleForDetailDTO.class);
        if(schedule.getInheritance()!=null){
            DriverForDetailDTO driverForDetailDTO = new DriverForDetailDTO();
//            driverForDetailDTO = driverForDetailDTO.mapToResponse(schedule.getInheritance().getDriver());
            scheduleForDetailDTO.setInheritance(driverForDetailDTO.mapToResponse(schedule.getDriver()));
            scheduleForDetailDTO.setDriver(driverForDetailDTO.mapToResponse(schedule.getInheritance().getDriver()));
        }
        return scheduleForDetailDTO;
    }

    public List<ScheduleForDetailDTO> mapToListResponse(List<Schedule> schedules) {
        return schedules.stream()
                .map(x -> mapToResponse(x))
                .collect(Collectors.toList());
    }
}
