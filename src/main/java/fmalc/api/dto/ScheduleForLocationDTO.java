package fmalc.api.dto;

import fmalc.api.entity.*;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.*;

@Data
public class ScheduleForLocationDTO {
    private Integer id;
    private int vehicle_id;
    private int driver_id;
    private ConsignmentResponseDTO consignment;
    private String imageConsignment;
    private String note;


}
