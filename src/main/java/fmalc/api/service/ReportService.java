package fmalc.api.service;

import java.util.HashMap;

public interface ReportService {
   HashMap<String, Integer> getOverviewReport();
   HashMap<Integer,Object> getReportByYear(int year);

}
