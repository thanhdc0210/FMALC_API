package fmalc.api.service;

import fmalc.api.dto.ReportBySpecificRangeResponseDTO;

import java.text.ParseException;
import java.util.HashMap;

public interface ReportService {
   HashMap<String, Integer> getOverviewReport();
   HashMap<Integer,Object> getReportByYear(int year) throws ParseException;
   ReportBySpecificRangeResponseDTO getReportOneVehicleBySpecificRange(Integer vehicleId, String startDate,String endDate) throws ParseException;

}
