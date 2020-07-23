package fmalc.api.service;


import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.dto.ScheduleForConsignmentDTO;
import fmalc.api.dto.VehicleForDetailDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Vehicle;

import java.sql.Timestamp;
import java.util.List;

public interface VehicleService {
    Vehicle saveVehicle(Vehicle vehicle);

    VehicleForDetailDTO findVehicleById(int id);

    Vehicle findVehicleByIdForLocation(int id);

    List<Vehicle> getListVehicle();

    Vehicle findVehicleByLicensePlates(String licensePlates);

    List<Vehicle> findByStatus(int status, double weight);

    List<Vehicle> findByWeight(double weight);


    List<Vehicle> findByWeightBigger(double weight);

    List<Vehicle> findByWeightSmaller(double weight);
//    List<Vehicle> findByWeight( double weight);
//    List<String> findVehicleLicensePlatesForReportInspection(List<Integer> status, String username, Timestamp currentDate);


    List<String> findVehicleLicensePlatesForReportInspection(List<Integer> status,
                                                             String username, Timestamp currentDate);

    //    List<String> findVehicleLicensePlatesForReportInspection(List<Integer> status, Integer driver_id, Timestamp currentDate);
    List<String> findVehicleLicensePlatesForReportInspection(List<Integer> status, String username);


    List<String> findVehicleLicensePlatesForReportInspectionBeforeDelivery(List<Integer> status,
                                                                           String username, Timestamp startDate, Timestamp endDate);

    List<String> findVehicleLicensePlatesForReportInspectionAfterDelivery(List<Integer> status,
                                                                          String username, Timestamp startDate, Timestamp endDate);

    Vehicle getVehicleByKmRunning(List<Vehicle> vehicles);

    List<Vehicle> findVehicleForSchedule(Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO,
                                         int statusSchedule);

    List<ScheduleForConsignmentDTO> findScheduleForFuture(List<Vehicle> vehicles,
                                                          Consignment consignment,
                                                          ConsignmentRequestDTO consignmentRequestDTO);

    List<Vehicle> checkScheduledForVehicle(List<Vehicle> vehicles, Consignment consignment);

    List<ScheduleForConsignmentDTO> checkScheduleForVehicle(int idVehicle);

    void updateStatus(int status, int id);

}
