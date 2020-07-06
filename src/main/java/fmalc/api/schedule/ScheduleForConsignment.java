package fmalc.api.schedule;

import fmalc.api.dto.*;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Driver;
import fmalc.api.entity.Place;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.DriverStatusEnum;
import fmalc.api.enums.TypeLocationEnum;

import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

public class ScheduleForConsignment {

    @Autowired
    MaintainanceService maintainanceService;
    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PlaceService placeService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    DriverService driverService;

    private static int priorityPlace = 1;

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
                List<PlaceResponeDTO> placeConsignmentPriorityRecei =
                        placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.RECEIVED_PLACE.getValue());

                //list place delivery of a consignment
                List<PlaceResponeDTO> placeConsgimentsPriorityDeli =
                        placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                //receive and priority =1
                String dateReceiConsignment = sdf.format(placeConsignmentPriorityRecei.get(placeConsignmentPriorityRecei.size()/placeConsignmentPriorityRecei.size()).getPlannedTime());
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

//        boolean flag = true;
//        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();
//        List<Integer> id = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        for (int i = 0; i < drivers.size(); i++) {
//            flag = true;
//            maintainCheckDTO = maintainanceService.checkMaintaiinForDriver(drivers.get(i).getId());
//            if (maintainCheckDTO.getId() != null) {
//                // check xem lich bao tri co trung ngay vs ngay giao hang khong
//                Place deliveryDetail =
//                        deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
//                PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
//                placeResponeDTO = placeResponeDTO.convertPlace(deliveryDetail);
//                String datePlace = sdf.format(placeResponeDTO.getPlannedTime());
//                String dateMaintain = sdf.format(maintainCheckDTO.getMaintainDate());
//                // ngay di lay hang sau ngay maintain
//                if (dateMaintain.compareTo(datePlace) <= -1) {
//                    flag = false;
//                }
//                // ngay lay hang trc ngay maintain lon hon 1 ngay
//                else if (dateMaintain.compareTo(datePlace) > 1) {
//                    Place placeReceive =
//                            deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.DELIVERED_PLACE.getValue());
//                    String dateReceice = sdf.format(placeReceive.getPlannedTime());
//                    // ngay tra hang trc ngay maintain
//                    if (dateMaintain.compareTo(dateReceice) < -1) {
//                        flag = false;
//                    }// ngay lay hang trc ngay maintain, ngay giao hang sau ngay maintain ( lich maintain dc len trc)
//                    else {
//                        id.add(drivers.get(i).getId());
//                    }
//                }
//                if (flag) {
//                    id.add(drivers.get(i).getId());
//                }
//            }
//        }
//        //remove driver
//        for (int j = 0; j < id.size(); j++) {
//            flag = true;
//            for (int i = 0; i < drivers.size(); i++) {
//                if (flag) {
//                    if (drivers.get(i).getId() == id.get(j)) {
//                        drivers.remove(drivers.get(i));
//                        flag = false;
//                    }
//                }
//            }
//        }
        return drivers;
    }

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
            scheduleForLocationDTOS = scheduleService.checkScheduleForVehicle(vehicles.get(i).getId());
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
                            placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());

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