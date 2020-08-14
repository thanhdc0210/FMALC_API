package fmalc.api.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class LocationDTO implements Serializable {

    private double latitude;
    private  double longitude;
    private String time;
    private int schedule;
    private String address;




}
