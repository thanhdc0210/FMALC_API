package fmalc.api.dto;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.Schedule;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class ConsignmentDetailDTO {
    private Integer amount;
    private Integer id;
    private Collection<ConsignmentHistoryDTO> consignmentHistories;
    private String ownerName;
    private String ownerNote;
    private Double weight;
    private Integer status;
    private String imageContract;
    private List<ScheduleForDetailDTO> schedules;
    private Collection<PlaceResponeDTO> places;

    public ConsignmentDetailDTO convertToDTO(Consignment consignment){
        ModelMapper modelMapper = new ModelMapper();
        ConsignmentDetailDTO consignmentDetailDTO = modelMapper.map(consignment, ConsignmentDetailDTO.class);
        List<ScheduleForDetailDTO> list = new ArrayList<>();
        ScheduleForDetailDTO scheduleForDetailDTO = new ScheduleForDetailDTO();
        list = scheduleForDetailDTO.mapToListResponse((List<Schedule>) consignment.getSchedules());
        consignmentDetailDTO.setSchedules(list);
        return consignmentDetailDTO;
    }

}
