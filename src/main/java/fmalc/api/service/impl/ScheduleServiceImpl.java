//package fmalc.api.service.impl;
//
//import fmalc.api.dto.ConsignmentListDTO;
//import fmalc.api.dto.ScheduleForLocationDTO;
//import fmalc.api.entity.Consignment;
//import fmalc.api.entity.Driver;
//import fmalc.api.entity.Schedule;
//import fmalc.api.entity.Vehicle;
//import fmalc.api.enums.VehicleStatusEnum;
//import fmalc.api.repository.ScheduleRepository;
//import fmalc.api.service.ConsignmentService;
//import fmalc.api.service.DriverService;
//import fmalc.api.service.ScheduleService;
//import fmalc.api.service.VehicleService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ScheduleServiceImpl implements ScheduleService {
//
//    @Autowired
//    ScheduleRepository scheduleRepository;
//
//    @Autowired
//    ConsignmentService consignmentService;
//
//    @Autowired
//    VehicleService vehicleService;
//
//    @Autowired
//    DriverService driverService;
//
//    @Override
//    public List<ScheduleForLocationDTO> getScheduleByConsignmentId(int id) {
//
//
//        //-----------------------------------------------------------------------
//        List<Schedule> schedules = scheduleRepository.findByConsignment_Id(id);
//        List<ScheduleForLocationDTO> scheduleForLocationDTOs= new ArrayList<>();
//        for(int i=0; i<schedules.size();i++){
//            ScheduleForLocationDTO scheduleForLocationDTO = convertScheduleResponse(schedules.get(i));
//            scheduleForLocationDTO.setVehicle_id(schedules.get(i).getVehicle().getId());
//            scheduleForLocationDTO.setDriver_id(schedules.get(i).getDriver().getId());
//            scheduleForLocationDTOs.add(scheduleForLocationDTO);
//        }
////
////         = mapToListResponse(schedules);
////
//
//        return  scheduleForLocationDTOs;
//    }
//
//    @Override
//    public Schedule createSchedule(Schedule schedule) {
//
//
//        return scheduleRepository.save(schedule);
//    }
//
//    private ScheduleForLocationDTO convertScheduleResponse(Schedule schedule) {
//        ModelMapper modelMapper = new ModelMapper();
//        ScheduleForLocationDTO dto = modelMapper.map(schedule, ScheduleForLocationDTO.class);
//
//        return dto;
//    }
//    public List<ScheduleForLocationDTO> mapToListResponse(List<Schedule> schedule) {
//        return schedule.stream()
//                .map(x -> convertScheduleResponse(x))
//                .collect(Collectors.toList());
//    }
//    public Driver findDriverForSchedule(){
//        List<Vehicle> vehicles = vehicleService.findByStatus(VehicleStatusEnum.AVAILABLE.getValue());
//        Driver driver = new Driver();
//        if(vehicles.size()>0){
//            Vehicle vehicle = vehicleService.getVehicleByKmRunning(vehicles);
//            double weight = vehicle.getWeight();
//           List<Driver> drivers =  driverService.getListDriverByLicense(weight);
//           float workingHours = drivers.get(0).getWorkingHour();
//           driver = drivers.get(0);
//           for (int i =1; i< drivers.size(); i++){
//               float tmp = drivers.get(i).getWorkingHour();
//               if(workingHours > tmp){
//                   driver = drivers.get(i);
//               }
//           }
//
//        }
//        return  driver;
////        List<Consignment> consignments = consignmentService.getAllByStatus(0);
//    }
//
//}
