package fmalc.api.service;



import fmalc.api.dto.VehicleForDetailDTO;
import fmalc.api.entity.Vehicle;

import java.sql.Timestamp;
import java.util.List;

public interface VehicleService {
    Vehicle saveVehicle (Vehicle vehicle);
    VehicleForDetailDTO findVehicleById(int id);
    Vehicle findVehicleByIdForLocation(int id);
    List<Vehicle> getListVehicle();
    Vehicle findVehicleByLicensePlates (String licensePlates);
    List<Vehicle> findByStatus(int status, double weight);
    List<Vehicle> findByWeight( double weight);
    String findVehicleLicensePlatesForReportInspectionBeforeDelivery(String username);
    String findVehicleLicensePlatesForReportInspectionAfterDelivery(String username, Timestamp startDate);
    Vehicle getVehicleByKmRunning(List<Vehicle> vehicles);
    void updateStatus(int status, int id);
    Vehicle findVehicleByUsernameAndConsignmentStatus(String username, List<Integer> status);
    List<Vehicle> findByWeightBigger( double weight);
    List<Vehicle> findByWeightSmaller( double weight);
}
