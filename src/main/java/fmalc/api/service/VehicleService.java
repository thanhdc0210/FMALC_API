package fmalc.api.service;


import fmalc.api.dto.*;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Vehicle;

import java.sql.Timestamp;
import java.util.List;

public interface VehicleService {
    Vehicle saveVehicle(Vehicle vehicle);
    Vehicle updateVehicle(Vehicle vehicle);
    Vehicle findById(int id);

    List<VehicleResponseDTO> findAll();

    VehicleForDetailDTO findVehicleById(int id);

    Vehicle findVehicleByIdForLocation(int id);

    Paging getListVehicle(int page, String license, int status);
    Vehicle disableVehicle(int id);
    Vehicle findVehicleByLicensePlates(String licensePlates);

    List<Vehicle> findByStatus(int status, double weight);

    Vehicle updateKmVehicle(int id, int km);

    List<Vehicle> findByWeight(double weight);


    List<Vehicle> findByWeightBigger(double weight);

    List<Vehicle> findByWeightSmaller(double weight);

    List<Vehicle> findVehicleForSchedule(Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO,
                                         int statusSchedule);

    List<ScheduleForConsignmentDTO> findScheduleForFuture(List<Vehicle> vehicles,
                                                          Consignment consignment,
                                                          ConsignmentRequestDTO consignmentRequestDTO);

    List<Vehicle> checkScheduledForVehicle(List<Vehicle> vehicles, Consignment consignment);

    List<ScheduleForConsignmentDTO> checkScheduleForVehicle(int idVehicle);

    int updateStatus(int status, int id);

    boolean checkLicensePlates(String licensePlates);


    // Get license plates for making report before running or while running
    String findLicensePlatesForMakingReportBeforeRunningOrWhileRunning(List<Integer> status, String username);

    // Get license plates for making report after running
    String findLicensePlatesForMakingReportAfterRunning(List<Integer> status, String username);

}
