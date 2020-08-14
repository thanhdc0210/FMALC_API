package fmalc.api.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class ConsignmentHistoryDTO {
    private Integer id;
    private Date time;
    private String note;
}
