package fmalc.api.dto;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.ConsignmentHistory;
import fmalc.api.entity.Place;
import fmalc.api.entity.Schedule;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.*;
import java.util.Collection;

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
        return consignmentDetailDTO;
    }

}
