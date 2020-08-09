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
    Vehicle disableVehicle(int id);
    Vehicle findVehicleByLicensePlates(String licensePlates);

    List<Vehicle> findByStatus(int status, double weight);
    String findVehicleLicensePlatesForReportInspectionBeforeDelivery(String username, Timestamp endDate);
    String findVehicleLicensePlatesForReportInspectionAfterDelivery(String username, Timestamp startDate);

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
    Vehicle findVehicleByUsernameAndConsignmentStatus(String username, List<Integer> status,
                                                      Timestamp startDate, Timestamp endDate);
}
