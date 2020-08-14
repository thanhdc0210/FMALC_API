package fmalc.api.service.impl;

import fmalc.api.dto.ReportBySpecificRangeResponseDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Place;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.repository.ConsignmentRepository;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.ReportService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    private static final String MONTH_YEAR_PATTERN = "MM-yyyy";
    private static final String DMY_PATTERN = "dd-MM-yyyy";

    @Override
    public HashMap<String, Integer> getOverviewReport() {
        HashMap<String, Integer> result = new HashMap<>();
        result.put("CONSIGNMENT", consignmentRepository.findAll().size());
        result.put("DRIVER", driverRepository.findAll().size());
        result.put("VEHICLE", vehicleRepository.findAll().size());
        return result;
    }


    @Override
    public HashMap<Integer, Object> getReportByYear(int year) throws ParseException {
        HashMap<Integer, Object> result = new HashMap<>();
        //set 12 months
        for (int i = 0; i <= 11; i++) {
            result.put(i, null);
        }
        ;
        //lấy list schedule có consignment là complete
        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList = scheduleRepository.findScheduleByConsignmentStatus(ConsignmentStatusEnum.COMPLETED.getValue());

        //filter year
        List<Schedule> listAfterFilterYear = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            long yearToCompare = schedule.getConsignment()
                    .getPlaces()
                    .stream()
                    .sorted(Comparator.comparing(Place::getActualTime))
                    .collect(toList())
                    .stream().findFirst().orElse(null).getActualTime().getTime();
            DateTime dateTime = new DateTime(yearToCompare);
            if (year == dateTime.getYear()) {
                listAfterFilterYear.add(schedule);
            }
        }

        //List group by month
        Map<Integer, List<Schedule>> groupByListMonth = listAfterFilterYear.stream()
                .collect(groupingBy(Schedule::getMonthForReport));


        //xét điều kiện trong tháng đó xe đã tồn tại hay không
        for (Map.Entry<Integer, List<Schedule>> oneMonth : groupByListMonth.entrySet()) {
            List<Schedule> listToFilter = oneMonth.getValue();
            Map<Vehicle, Long> filterVehicle = listToFilter.stream()
                    .collect(Collectors.groupingBy(Schedule::getVehicle, Collectors.counting()));
            Long monthYear = listToFilter.get(0).getTimeStampForReport().getTime();
            DateTime date = new DateTime(monthYear);
            String dateString = date.toString(MONTH_YEAR_PATTERN);
            dateString = +getLastDayOfMonth(dateString) + "-" + dateString;
            SimpleDateFormat formatter = new SimpleDateFormat(DMY_PATTERN);
            formatter.setTimeZone(TimeZone.getTimeZone(""));
            Date dateFormatted = formatter.parse(dateString);


            filterVehicle.forEach((k, v) -> {
                java.util.Date utilDate = new java.util.Date(k.getDateCreate().getTime());
                if (utilDate.after(dateFormatted)) {
                    filterVehicle.remove(k);
                }
            });

            java.sql.Date sqlDate = new java.sql.Date(dateFormatted.getTime());
            int total = vehicleRepository.findByDateCreateBefore(sqlDate).size();
            ObjectForReportVehicle object = new ObjectForReportVehicle(total, filterVehicle.size());
            result.put(oneMonth.getKey(), object);
//            Schedule schedule = listToFilter.get(0).getSchedule().getTimeStampForReport().get;

        }
        //

        return result;
    }

    @Override
    public ReportBySpecificRangeResponseDTO getReportOneVehicleBySpecificRange(Integer vehicleId, String startDate, String endDate, Integer status) throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date localDateStartTime =formatter.parse(startDate);
        Date localDateEndTime = formatter.parse(endDate);


        List<Consignment> consignmentList = new ArrayList<>();
        consignmentList = consignmentRepository.getConsignmentForReport(new Timestamp(localDateStartTime.getTime()),new Timestamp(localDateEndTime.getTime()), status);
        AtomicInteger rate = new AtomicInteger();


        ReportBySpecificRangeResponseDTO result = new ReportBySpecificRangeResponseDTO();
        if (consignmentList.size() > 0) {

            consignmentList.forEach(e -> e.getSchedules().forEach(schedule -> {
                if (schedule.getVehicle().getId().equals(vehicleId)) {
                    rate.getAndIncrement();
                }
            }));
            result.setRate(rate.get());
            result.setTotalConsignment(consignmentList.size());
        }
        return result;
    }

    public class ObjectForReportVehicle {
        Integer total;
        Integer vehicleInOneMonth;

        public ObjectForReportVehicle(Integer total, Integer vehicleInOneMonth) {
            this.total = total;
            this.vehicleInOneMonth = vehicleInOneMonth;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getVehicleInOneMonth() {
            return vehicleInOneMonth;
        }

        public void setVehicleInOneMonth(Integer vehicleInOneMonth) {
            this.vehicleInOneMonth = vehicleInOneMonth;
        }
    }


    public int getLastDayOfMonth(String dateString) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(MONTH_YEAR_PATTERN);
        YearMonth yearMonth = YearMonth.parse(dateString, pattern);
        LocalDate date = yearMonth.atEndOfMonth();
        return date.lengthOfMonth();
    }
}

