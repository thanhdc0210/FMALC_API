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
    List<Vehicle> findByStatus(int status);

    List<String> findVehicleLicensePlatesForReportInspection(List<Integer> status, String username, Timestamp currentDate);
    Vehicle getVehicleByKmRunning(List<Vehicle> vehicles);
    void updateStatus(int status, int id);
}
