package fmalc.api.dto;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.FleetManager;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
public class ConsignmentHistoryDTO {
    private Integer id;
    private Date time;
    private String note;
}
