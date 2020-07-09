package fmalc.api.dto;

import fmalc.api.entity.Driver;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.*;

@Data
public class ScheduleToConfirmDTO {
    private Integer id;
    private List<VehicleForDetailDTO> vehicles;
    private List<DriverForScheduleDTO> drivers;
    private List<VehicleForDetailDTO> vehicleForDetailDTOS;
    private List<DriverForScheduleDTO> driverForScheduleDTOS;
    private ConsignmentResponseDTO consignment;
    private String imageConsignment;
    private String note;

    public ScheduleToConfirmDTO convertSchedule(Schedule schedule){
        ModelMapper modelMapper = new ModelMapper();
        ScheduleToConfirmDTO scheduleForLocationDTO = modelMapper.map(schedule, ScheduleToConfirmDTO.class);
        return  scheduleForLocationDTO;
    }
}
