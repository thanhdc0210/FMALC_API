package fmalc.api.dto;

import java.io.Serializable;
import java.sql.Timestamp;


public class LocationDTO implements Serializable {

    private double latitude;
    private  double longitude;
    private String time;
    private int vehicle_id;

    public LocationDTO() {
    }

    public LocationDTO(double latitude, double longitude, String time, int vehicle_id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.vehicle_id = vehicle_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }
}
