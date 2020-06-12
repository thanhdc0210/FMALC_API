package fmalc.api.dto;

import java.io.Serializable;

public class VehicleDTO implements Serializable {
    private String currentLocation;


    private String dateOfManufacture;


    private Integer id;


    private Integer kilometerRunning;

    /**
     * Biển số xe
     */

    private String licensePlates;

    private double weight;
    private int vehicleTypeId;

    public VehicleDTO(String currentLocation, String dateOfManufacture, Integer id, Integer kilometerRunning, String licensePlates, double weight, int vehicleTypeId) {
        this.currentLocation = currentLocation;
        this.dateOfManufacture = dateOfManufacture;
        this.id = id;
        this.kilometerRunning = kilometerRunning;
        this.licensePlates = licensePlates;
        this.weight = weight;
        this.vehicleTypeId = vehicleTypeId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getDateOfManufacture() {
        return dateOfManufacture;
    }

    public void setDateOfManufacture(String dateOfManufacture) {
        this.dateOfManufacture = dateOfManufacture;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKilometerRunning() {
        return kilometerRunning;
    }

    public void setKilometerRunning(Integer kilometerRunning) {
        this.kilometerRunning = kilometerRunning;
    }

    public String getLicensePlates() {
        return licensePlates;
    }

    public void setLicensePlates(String licensePlates) {
        this.licensePlates = licensePlates;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }
}
