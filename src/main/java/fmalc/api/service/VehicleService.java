package fmalc.api.service;

import fmalc.api.entity.Consignment;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface VehicleService {
    List<String> findVehicleLicensePlatesForReportInspection(List<Integer> status, String username, Timestamp currentDate);
}
