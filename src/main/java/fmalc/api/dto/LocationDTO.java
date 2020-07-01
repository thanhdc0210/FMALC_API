package fmalc.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class LocationDTO implements Serializable {

    private double latitude;
    private  double longitude;
    private String time;
    private int consignment;
    private String address;


}
