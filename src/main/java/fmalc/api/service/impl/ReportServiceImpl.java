package fmalc.api.service.impl;

import fmalc.api.entity.Place;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.repository.ConsignmentRepository;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
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


    @Override
    public HashMap<String, Integer> getOverviewReport() {
        HashMap<String,Integer> result = new HashMap<>();
        result.put("CONSIGNMENT",consignmentRepository.findAll().size());
        result.put("DRIVER",driverRepository.findAll().size());
        result.put("VEHICLE",vehicleRepository.findAll().size());
        return result;
    }


    @Override
    public HashMap<Integer, Object> getReportByYear(int year) {
        HashMap<Integer,Object> result = new HashMap<>();
        //set 12 months
        for (int i=0; i <= 11; i++){
            result.put(i,null);
        };
        //lấy list schedule có consignment là complete
        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList = scheduleRepository.findScheduleByConsignmentStatus(ConsignmentStatusEnum.COMPLETED.getValue());

        //filter year
        List<Schedule> listAfterFilterYear = new ArrayList<>();
        for( Schedule schedule : scheduleList){
             int yearToCompare =  schedule.getConsignment()
                    .getPlaces()
                    .stream()
                    .sorted(Comparator.comparing(Place::getActualTime))
                    .collect(toList())
                     .stream().findFirst().orElse(null).getActualTime().getYear();
             if (year == yearToCompare){
                listAfterFilterYear.add(schedule);
            }
        }

        //List group by month
        Map<Integer, List<Schedule>>  groupByListMonth = listAfterFilterYear .stream()
                .collect(groupingBy(Schedule::getMonthForReport));


        //xét điều kiện trong tháng đó xe đã tồn tại hay không
        for (Map.Entry<Integer, List<Schedule>> oneMonth : groupByListMonth.entrySet()){
            List<Schedule> listToFilter = oneMonth.getValue();
            Map<Vehicle, Long> filterVehicle = listToFilter.stream()
                    .collect(Collectors.groupingBy(Schedule::getVehicle,Collectors.counting()));
            Date date = null;
            date.setMonth(oneMonth.getKey());
            date.setYear(year);
            filterVehicle.forEach((k,v)-> {
               if (k.getDateCreate().after(date)){
                   filterVehicle.remove(k);
               }
            });
            int total = vehicleRepository.findByDateCreateBefore(date).size();
            ObjectForReportVehicle object = new ObjectForReportVehicle(total,filterVehicle.size());
            result.put(oneMonth.getKey(),object);
//            Schedule schedule = listToFilter.get(0).getSchedule().getTimeStampForReport().get;

        }
        //



        return result;
    }

    public class  ObjectForReportVehicle{
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
}

