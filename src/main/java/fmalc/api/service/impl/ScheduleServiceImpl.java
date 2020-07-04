
package fmalc.api.service.impl;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.DriverStatusEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    MaintainanceService maintainanceService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PlaceService placeService;



    private static int priorityPlace = 1;

    @Override
    public List<ScheduleForLocationDTO> getScheduleByConsignmentId(int id) {
        //-----------------------------------------------------------------------
        ScheduleForLocationDTO scheduleForLocationDTO = new ScheduleForLocationDTO();
        List<Schedule> schedules = scheduleRepository.findByConsignment_Id(id);
        List<ScheduleForLocationDTO> scheduleForLocationDTOs = new ArrayList<>();
        for (int i = 0; i < schedules.size(); i++) {
           scheduleForLocationDTO = scheduleForLocationDTO.convertSchedule(schedules.get(i));
            scheduleForLocationDTO.setVehicle_id(schedules.get(i).getVehicle().getId());
            scheduleForLocationDTO.setDriver_id(schedules.get(i).getDriver().getId());
            scheduleForLocationDTOs.add(scheduleForLocationDTO);
        }
        return scheduleForLocationDTOs;
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {

        return scheduleRepository.save(schedule);


    }


    //find vehicle with status = available
    @Override
    public Vehicle findVehicleForSchedule(Consignment consignment) {
//        ScheduleForConsignment scheduleForConsignment = new ScheduleForConsignment();
        boolean flag = true;
        List<Vehicle> vehiclesAvailable = vehicleService.findByStatus(VehicleStatusEnum.AVAILABLE.getValue(), consignment.getWeight());
        List<Vehicle> vehiclesScheduled = vehicleService.findByStatus(VehicleStatusEnum.SCHEDULED.getValue(), consignment.getWeight());
        Vehicle vehicle = new Vehicle();
        List<Vehicle> vehicles = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if (vehiclesAvailable.size() > 0) {
            vehiclesAvailable= checkMaintainForVehicle(vehiclesAvailable,consignment);
            vehiclesAvailable = checkScheduledForVehicle(vehiclesAvailable,consignment);
            vehicle = vehicleService.getVehicleByKmRunning(vehiclesAvailable);
        }
        return vehicle;
    }

    @Override
    public Driver findDriverForSchedule(Vehicle vehicle, Consignment consignment) {
        VehicleReponseDTO vehicleReponseDTO = new VehicleReponseDTO();
        vehicleReponseDTO = vehicleReponseDTO.convertVehicle(vehicle);
        Driver driver = new Driver();
        List<Driver> drivers = new ArrayList<>();
//        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();
        drivers = driverService.getListDriverByLicense(vehicleReponseDTO.getWeight(), DriverStatusEnum.AVAILABLE.getValue());
        if (drivers.size() > 0) {
            drivers = checkMaintainForDriver(drivers, consignment);

            if (drivers.size() > 0) {
                driver = Collections.min(drivers, Comparator.comparing(s -> s.getWorkingHour()));
            }
        }
        return driver;
    }


//    find vehicle with status = schedule
    @Override
    public  List<ScheduleForLocationDTO> checkScheduleForVehicle(int idVehicle) {
        List<ScheduleForLocationDTO> scheduleForLocationDTOS = new ArrayList<>();
        ScheduleForLocationDTO scheduleForLocationDTO = new ScheduleForLocationDTO();
        List<Integer> id = new ArrayList<>();
        boolean flag;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
//        date = sdf.parse(sdf.format(date));
//        java.sql.Date date1 = new java.sql.Date(date.getTime());
        List<Schedule> Schedules = scheduleRepository.checkVehicleInScheduled(idVehicle);
        if(Schedules.size()>0){

            scheduleForLocationDTOS = scheduleForLocationDTO.mapToListResponse(Schedules);
            if(scheduleForLocationDTOS.size()>0){
                for(int i=0; i< scheduleForLocationDTOS.size();i++){
                    PlaceResponeDTO deliveryDetail =
                            placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTOS.get(i).getConsignment().getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
//                    PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
//                    placeResponeDTO = placeResponeDTO.convertPlace(deliveryDetail);
                    String datePlace = sdf.format(deliveryDetail.getPlannedTime());
                    String dateNow = sdf.format(new Date());
                    if(dateNow.compareTo(datePlace)<=0){
                        id.add(scheduleForLocationDTOS.get(i).getId());
                    }
                }
                for (int j = 0; j < id.size(); j++) {
                    flag = true;
                    for (int i = 0; i < scheduleForLocationDTOS.size(); i++) {
                        if(flag){
                            if (scheduleForLocationDTOS.get(i).getId() == id.get(j)) {
                                scheduleForLocationDTOS.remove(scheduleForLocationDTOS.get(i));
                                flag = false;
                            }
                        }
                    }

                }

            }


        }
        return scheduleForLocationDTOS;
    }

    @Override
    public  List<ScheduleForLocationDTO> checkMaintainForDriver(int idDriver) {
        return null;
    }

    public List<Vehicle> checkMaintainForVehicle(List<Vehicle> vehicles, Consignment consignment) {

        boolean flag = true;
        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();
        List<Integer> id = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        for (int i = 0; i < vehicles.size(); i++) {
            flag = true;
            double weight = consignment.getWeight();
            VehicleForDetailDTO vehicle = vehicleService.findVehicleById(vehicles.get(i).getId());
            //check xe co lich bao tri trong tuong lai
            maintainCheckDTO = maintainanceService.checkMaintainForVehicle(vehicles.get(i).getId());
            if (maintainCheckDTO.getId() != null) {


                //list place receive of a consignment
                PlaceResponeDTO placeSchedulePriorityRecei =
                        placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), 1, TypeLocationEnum.DELIVERED_PLACE.getValue());

                //list place delivery of a consignment
                List<PlaceResponeDTO> placeConsgimentsPriorityDeli =
                        placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                //receive and priority =1
                String dateReceiConsignment = sdf.format(placeSchedulePriorityRecei.getPlannedTime());
                String dateMaintain = sdf.format(maintainCheckDTO.getPlannedMaintainDate());
                if (dateReceiConsignment.compareTo(dateMaintain) <= 1) {
                    PlaceResponeDTO placeSchedulePriorityDeli =
                            placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), placeConsgimentsPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                    String dateConsignemntDeli = sdf.format(placeSchedulePriorityDeli.getPlannedTime());
                    if (dateConsignemntDeli.compareTo(dateMaintain) <= 1) {
                        flag = false;
                        id.add(vehicles.get(i).getId());
                        //xe pass 1 schedule;
                    }
                } else if (dateReceiConsignment.compareTo(dateMaintain) >= 1) {
                    flag=false;
                    id.add(vehicles.get(i).getId());
                }

            }
        }
        for (Integer integer : id) {

            for (int i = 0; i < vehicles.size(); i++) {

                if (vehicles.get(i).getId() == integer) {
                    vehicles.remove(vehicles.get(i));
                    i = vehicles.size();
                }

            }
        }
        return vehicles;
    }

    public List<Driver> checkMaintainForDriver(List<Driver> drivers, Consignment consignment) {

        boolean flag = true;
        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();
        List<Integer> id = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        for (int i = 0; i < drivers.size(); i++) {
            flag = true;
            double weight = consignment.getWeight();
            VehicleForDetailDTO vehicle = vehicleService.findVehicleById(drivers.get(i).getId());
            //check xe co lich bao tri trong tuong lai
            maintainCheckDTO = maintainanceService.checkMaintainForVehicle(drivers.get(i).getId());
            if (maintainCheckDTO.getId() != null) {


                //list place receive of a consignment
                PlaceResponeDTO placeSchedulePriorityRecei =
                        placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), 1, TypeLocationEnum.DELIVERED_PLACE.getValue());

                //list place delivery of a consignment
                List<PlaceResponeDTO> placeConsgimentsPriorityDeli =
                        placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                //receive and priority =1
                String dateReceiConsignment = sdf.format(placeSchedulePriorityRecei.getPlannedTime());
                String dateMaintain = sdf.format(maintainCheckDTO.getPlannedMaintainDate());
                if (dateReceiConsignment.compareTo(dateMaintain) <= 1) {
                    PlaceResponeDTO placeSchedulePriorityDeli =
                            placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), placeConsgimentsPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                    String dateConsignemntDeli = sdf.format(placeSchedulePriorityDeli.getPlannedTime());
                    if (dateConsignemntDeli.compareTo(dateMaintain) <= 1) {
                        flag = false;
                        id.add(drivers.get(i).getId());
                        //xe pass 1 schedule;
                    }
                } else if (dateReceiConsignment.compareTo(dateMaintain) >= 1) {
                    flag=false;
                    id.add(drivers.get(i).getId());
                }

            }
        }
        for (Integer integer : id) {

            for (int i = 0; i < drivers.size(); i++) {

                if (drivers.get(i).getId() == integer) {
                    drivers.remove(drivers.get(i));
                    i = drivers.size();
                }

            }
        }
        return drivers;
    }



    public List<Vehicle> checkScheduledForVehicle(List<Vehicle> vehicles, Consignment consignment) {

        boolean flag = true;
        List<ScheduleForLocationDTO> scheduleForLocationDTOS = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<String> dateConsignmentSchedule = new ArrayList<>();
        List<String> dateConsignmentNew = new ArrayList<>();
        ScheduleForLocationDTO scheduleForLocationDTO = new ScheduleForLocationDTO();
        for (int i = 0; i < vehicles.size(); i++) {
            flag = true;
            double weight = consignment.getWeight();
            //check xe co lich bao tri trong tuong lai
            scheduleForLocationDTOS = checkScheduleForVehicle(vehicles.get(i).getId());
            if (scheduleForLocationDTOS.size() > 0) {
                for (int j = 0; j < scheduleForLocationDTOS.size(); j++) {

                    scheduleForLocationDTO = scheduleForLocationDTOS.get(j);


                    //list place receive of a schedule
                    List<PlaceResponeDTO> placeSchedulesPriority =
                            placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.RECEIVED_PLACE.getValue());

                    //list place receive of a consignment
                    List<PlaceResponeDTO> placeConsignmentPriority =
                            placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.RECEIVED_PLACE.getValue());

                    //list place delivery of a schedule
                    List<PlaceResponeDTO> placeSchedulesPriorityDeli =
                            placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                    //list place delivery of a consignment
                    List<PlaceResponeDTO> placeConsgimentsPriorityDeli =
                            placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());


                    PlaceResponeDTO placeSchedulesPriorityTmp =
                            placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), placeSchedulesPriority.size()/placeSchedulesPriority.size(), TypeLocationEnum.RECEIVED_PLACE.getValue());

                    String dateReceiConsignment = sdf.format(placeConsignmentPriority.get(placeConsignmentPriority.size()/placeConsignmentPriority.size()).getPlannedTime());
                    String dateSchedule = sdf.format(placeSchedulesPriorityTmp.getPlannedTime());
                    if (dateReceiConsignment.compareTo(dateSchedule) >= 1) {
                        PlaceResponeDTO placeSchedulePriorityDeli =
                                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), placeSchedulesPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                        String dateScheduleDe = sdf.format(placeSchedulePriorityDeli.getPlannedTime());
                        if (dateReceiConsignment.compareTo(dateScheduleDe) >= 1) {
                            flag = false;
//                                    l = placeConsignmentPriority.size();// out for check time
                            //xe pass 1 schedule;
                        }
                    } else if (dateReceiConsignment.compareTo(dateSchedule) <= -1) {
                        PlaceResponeDTO placeConsignmentPriorityDeli =
                                placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), placeConsgimentsPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                        String dateConsignmentDeli = sdf.format(placeConsignmentPriorityDeli.getPlannedTime());
                        if (dateConsignmentDeli.compareTo(dateSchedule) <= -1) {
                            flag = false;
                            // xe pass 1 schedule
                        }
                    }


                    if(flag ){
                        id.add(vehicles.get(i).getId());
                        j = scheduleForLocationDTOS.size();
                    }
                }
            } else {

            }

        }
        for (Integer integer : id) {
            for (int i = 0; i < vehicles.size(); i++) {

                if (vehicles.get(i).getId() == integer) {
                    vehicles.remove(vehicles.get(i));
                    i = vehicles.size();
                }
            }
        }
        return vehicles;
    }


}

