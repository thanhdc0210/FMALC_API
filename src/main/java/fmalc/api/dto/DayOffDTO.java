package fmalc.api.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class DayOffDTO {
    private String dateStart;
    private String dateEnd;
    private int idDriver;
    private int id;
}
