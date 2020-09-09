package fmalc.api.dto;


import lombok.Data;

import java.util.HashMap;
import java.util.logging.Handler;

@Data
public class ReportBySpecificRangeResponseDTO {

    private Integer totalConsignment;
    private Integer rate;
    private HashMap<Integer, Double> averageFuelByMonth;

}
