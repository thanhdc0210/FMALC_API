package fmalc.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class LocationResponeDTO implements Serializable {
    private int id;
    private double latitude;
    private  double longitude;
    private String time;
    private String address;
    private int consignment_id;


}
