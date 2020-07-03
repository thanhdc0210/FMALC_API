package fmalc.api.schedule;

import fmalc.api.dto.*;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Driver;
import fmalc.api.entity.Place;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.DriverStatusEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.service.DeliveryDetailService;
import fmalc.api.service.DriverService;
import fmalc.api.service.MaintainanceService;
import fmalc.api.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

public class ScheduleForConsignment {

    @Autowired
    MaintainanceService maintainanceService;
    @Autowired
    ScheduleService scheduleService;

    @Autowired
    DeliveryDetailService deliveryDetailService;

    @Autowired
    DriverService driverService;

    private static int priorityPlace =1;

    public List<Vehicle> checkMaintainForVehicle(List<Vehicle> vehicles, Consignment consignment) {

        boolean flag = true;
        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();
        List<Integer> id = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        for (int i = 0; i < vehicles.size(); i++) {
            flag = true;
            double weight = consignment.getWeight();
            //check xe co lich bao tri trong tuong lai
            maintainCheckDTO = maintainanceService.checkMaintainForVehicle(vehicles.get(i).getId());
            if (maintainCheckDTO.getId() != null) {
                // check xem lich bao tri co trung ngay vs ngay giao hang khong
                Place deliveryDetail =
                        deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
                PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
                placeResponeDTO = placeResponeDTO.convertPlace(deliveryDetail);
                String datePlace = sdf.format(placeResponeDTO.getPlannedTime());
                String dateMaintain = sdf.format(maintainCheckDTO.getMaintainDate());
                // ngay di lay hang sau ngay maintain
                if (dateMaintain.compareTo(datePlace) <= -1) {
                    flag = false;
                }
                // ngay lay hang trc ngay maintain lon hon 1 ngay
                else if (dateMaintain.compareTo(datePlace) > 1) {
                    Place placeReceive =
                            deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.DELIVERED_PLACE.getValue());
                    String dateReceice = sdf.format(placeReceive.getPlannedTime());
                    // ngay tra hang trc ngay maintain
                    if (dateMaintain.compareTo(dateReceice) < -1) {
                        flag = false;
                    }// ngay lay hang trc ngay maintain, ngay giao hang sau ngay maintain ( lich maintain dc len trc)
                    else {
                        id.add(vehicles.get(i).getId());
                    }
                }
                if (flag) {
                    id.add(vehicles.get(i).getId());
                }
            }
        }
        for (Integer integer : id) {
            flag = true;
            for (int i = 0; i < vehicles.size(); i++) {
                if (flag) {
                    if (vehicles.get(i).getId() == integer) {
                        vehicles.remove(vehicles.get(i));
                        flag = false;
                    }
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
            maintainCheckDTO = maintainanceService.checkMaintaiinForDriver(drivers.get(i).getId());
            if (maintainCheckDTO.getId() != null) {
                // check xem lich bao tri co trung ngay vs ngay giao hang khong
                Place deliveryDetail =
                        deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
                PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
                placeResponeDTO = placeResponeDTO.convertPlace(deliveryDetail);
                String datePlace = sdf.format(placeResponeDTO.getPlannedTime());
                String dateMaintain = sdf.format(maintainCheckDTO.getMaintainDate());
                // ngay di lay hang sau ngay maintain
                if (dateMaintain.compareTo(datePlace) <= -1) {
                    flag = false;
                }
                // ngay lay hang trc ngay maintain lon hon 1 ngay
                else if (dateMaintain.compareTo(datePlace) > 1) {
                    Place placeReceive =
                            deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.DELIVERED_PLACE.getValue());
                    String dateReceice = sdf.format(placeReceive.getPlannedTime());
                    // ngay tra hang trc ngay maintain
                    if (dateMaintain.compareTo(dateReceice) < -1) {
                        flag = false;
                    }// ngay lay hang trc ngay maintain, ngay giao hang sau ngay maintain ( lich maintain dc len trc)
                    else {
                        id.add(drivers.get(i).getId());
                    }
                }
                if (flag) {
                    id.add(drivers.get(i).getId());
                }
            }
        }
        //remove driver
        for (int j = 0; j < id.size(); j++) {
            flag = true;
            for (int i = 0; i < drivers.size(); i++) {
                if (flag) {
                    if (drivers.get(i).getId() == id.get(j)) {
                        drivers.remove(drivers.get(i));
                        flag = false;
                    }
                }
            }
        }
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
            drivers = checkMaintainForDriver(drivers,consignment);

            if (drivers.size() > 0) {
                driver = Collections.min(drivers, Comparator.comparing(s -> s.getWorkingHour()));
            }
        }
        return driver;
    }

//    public String checkDateReceiveCondition(int consignment){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        Place placeSchedule =
//                deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment, priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
//        PlaceResponeDTO placeScheduleDTO = new PlaceResponeDTO();
//        placeScheduleDTO = placeScheduleDTO.convertPlace(placeSchedule);
//        String datePlace = sdf.format(placeScheduleDTO.getPlannedTime());
//        return datePlace;
//    }
//    public String checkDateDeliveryCondition(int consignment, int){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        Place placeSchedule =
//                deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment, priorityPlace, TypeLocationEnum.DELIVERED_PLACE.getValue());
//        PlaceResponeDTO placeScheduleDTO = new PlaceResponeDTO();
//        placeScheduleDTO = placeScheduleDTO.convertPlace(placeSchedule);
//        String datePlace = sdf.format(placeScheduleDTO.getPlannedTime());
//        return datePlace;
//    }
    public List<String> prorityPlace(int idConsignemnt, int typeDelivery){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<String> stringDates = new ArrayList<>();
        List<DeliveryDetailResponseDTO> deliveryDetailResponseDTOs =
                deliveryDetailService.getDeliveryDetailByConsignment(idConsignemnt);
        for(int i =0; i< deliveryDetailResponseDTOs.size();i++){
            Place placeSchedule =
                    deliveryDetailService.getDeliveryByConsignmentAndPriority(deliveryDetailResponseDTOs.get(i).getConsignment(), deliveryDetailResponseDTOs.get(i).getPriority(), typeDelivery);
            PlaceResponeDTO placeScheduleDTO = new PlaceResponeDTO();
            placeScheduleDTO = placeScheduleDTO.convertPlace(placeSchedule);
            String datePlace = sdf.format(placeScheduleDTO.getPlannedTime());
            stringDates.add(datePlace);
        }
        return  stringDates;
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
            if (scheduleForLocationDTOS.size()>0) {
                for(int j=0; j<scheduleForLocationDTOS.size();j++){
                    scheduleForLocationDTO = scheduleForLocationDTOS.get(j);

                    List<DeliveryDetailResponseDTO> deliveryDetailSchedule =
                            deliveryDetailService.getDeliveryDetailByConsignment(scheduleForLocationDTO.getConsignment().getId());

                    List<DeliveryDetailResponseDTO> deliveryDetailConsignment =
                            deliveryDetailService.getDeliveryDetailByConsignment(consignment.getId());
                    int sizeConsginment = deliveryDetailConsignment.size();
                    int numPriority = 0;
                    if(sizeConsginment%2 != 0){
                        numPriority = (sizeConsginment/2)+1;
                    }else{
                        numPriority = (sizeConsginment/2);
                    }
                    for(int k = 1; k<= deliveryDetailConsignment.size();k++){
                        if(deliveryDetailConsignment.get(k).getPriority() == 1){
                            Place palceConsginment =
                                    deliveryDetailService.
                                            getDeliveryByConsignmentAndPriority
                                                    (deliveryDetailSchedule.get(k).getConsignment(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());
                            PlaceResponeDTO palceConsginmentDTO = new PlaceResponeDTO();
                            palceConsginmentDTO = palceConsginmentDTO.convertPlace(palceConsginment);
                            String datePlace = sdf.format(palceConsginmentDTO.getPlannedTime());

                            Place placeSchedule =
                                    deliveryDetailService.
                                            getDeliveryByConsignmentAndPriority
                                                    (deliveryDetailSchedule.get(k).getConsignment(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());
                            PlaceResponeDTO placeScheduleDTO = new PlaceResponeDTO();
                            placeScheduleDTO = placeScheduleDTO.convertPlace(placeSchedule);
                            String dateSchedule = sdf.format(placeScheduleDTO.getPlannedTime());

                            if(datePlace.compareTo(dateSchedule)>=1){

                            }
                        }


                    }



                    //consignment da tao
//                    Place placeSchedule =
//                            deliveryDetailService.getDeliveryByConsignmentAndPriority(scheduleForLocationDTOS.get(i).getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
//                    PlaceResponeDTO placeScheduleDTO = new PlaceResponeDTO();
//                    placeScheduleDTO = placeScheduleDTO.convertPlace(placeSchedule);
//                    String datePlace = sdf.format(placeScheduleDTO.getPlannedTime());
//
//                    //consignment dang tao
//                    Place placeConsignemnt =
//                            deliveryDetailService.getDeliveryByConsignmentAndPriority(scheduleForLocationDTOS.get(i).getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
//                    PlaceResponeDTO placeConsignemntDTO = new PlaceResponeDTO();
//                    placeConsignemntDTO = placeConsignemntDTO.convertPlace(placeConsignemnt);
//                    String dateConsignment= sdf.format(placeConsignemntDTO.getPlannedTime());
//
//                    if(dateConsignment.compareTo(datePlace)<=1){
//
//                        Place receiveSchedule =
//                                deliveryDetailService.getDeliveryByConsignmentAndPriority(scheduleForLocationDTOS.get(i).getId(), priorityPlace, TypeLocationEnum.DELIVERED_PLACE.getValue());
//                        PlaceResponeDTO receiveScheduleDTO = new PlaceResponeDTO();
//                        receiveScheduleDTO = receiveScheduleDTO.convertPlace(receiveSchedule);
//                        String dateRecieve = sdf.format(receiveScheduleDTO.getPlannedTime());
//                        if(dateRecieve.compareTo(datePlace)<=1){
//
//                        }else{
//                            id.add(vehicles.get(i).getId());
//                        }
//                    }else{
//                        if()
//                    }
                }
            }else{

            }
        }
        for (Integer integer : id) {
            flag = true;
            for (int i = 0; i < vehicles.size(); i++) {
                if (flag) {
                    if (vehicles.get(i).getId() == integer) {
                        vehicles.remove(vehicles.get(i));
                        flag = false;
                    }
                }
            }
        }
        return vehicles;
    }

    public Driver findDriver(Vehicle vehicle, Consignment consignment, List<Driver> drivers) {
        VehicleReponseDTO vehicleReponseDTO = new VehicleReponseDTO();
        vehicleReponseDTO = vehicleReponseDTO.convertVehicle(vehicle);
        Driver driver = new Driver();
        List<Driver> driversAvailable = new ArrayList<>();
        List<Driver> driversMaintain = new ArrayList<>();
        List<Driver> driversScheduled = new ArrayList<>();
        List<Driver> driversRunning = new ArrayList<>();
        List<Driver> driversUnavailable = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        boolean flag;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();

        for (int i = 0; i < drivers.size(); i++) {
            if (drivers.get(i).getStatus() == DriverStatusEnum.AVAILABLE.getValue()) {
                driversAvailable.add(drivers.get(i));
            } else if (drivers.get(i).getStatus() == DriverStatusEnum.MAINTAIN.getValue()) {
                driversMaintain.add(drivers.get(i));
            } else if (drivers.get(i).getStatus() == DriverStatusEnum.SCHEDULED.getValue()) {
                driversScheduled.add(drivers.get(i));
            } else if (drivers.get(i).getStatus() == DriverStatusEnum.RUNNING.getValue()) {
                driversRunning.add(drivers.get(i));
            } else if (drivers.get(i).getStatus() == DriverStatusEnum.UNAVAILABLE.getValue()) {
                driversUnavailable.add(drivers.get(i));
            }
        }


        drivers = driverService.getListDriverByLicense(vehicleReponseDTO.getWeight(), DriverStatusEnum.AVAILABLE.getValue());
        if (drivers.size() > 0) {
            for (int i = 0; i < drivers.size(); i++) {
                flag = true;
                maintainCheckDTO = maintainanceService.checkMaintaiinForDriver(drivers.get(i).getId());
                if (maintainCheckDTO.getId() != null) {
                    // check xem lich bao tri co trung ngay vs ngay giao hang khong
                    Place deliveryDetail =
                            deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
                    PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
                    placeResponeDTO = placeResponeDTO.convertPlace(deliveryDetail);
                    String datePlace = sdf.format(placeResponeDTO.getPlannedTime());
                    String dateMaintain = sdf.format(maintainCheckDTO.getMaintainDate());
                    // ngay di lay hang sau ngay maintain
                    if (dateMaintain.compareTo(datePlace) <= -1) {
                        flag = false;
                    }
                    // ngay lay hang trc ngay maintain lon hon 1 ngay
                    else if (dateMaintain.compareTo(datePlace) > 1) {
                        Place placeReceive =
                                deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.DELIVERED_PLACE.getValue());
                        String dateReceice = sdf.format(placeReceive.getPlannedTime());
                        // ngay tra hang trc ngay maintain
                        if (dateMaintain.compareTo(dateReceice) < -1) {
                            flag = false;
                        }// ngay lay hang trc ngay maintain, ngay giao hang sau ngay maintain ( lich maintain dc len trc)
                        else {
                            id.add(drivers.get(i).getId());
                        }
                    }
                    if (flag) {
                        id.add(drivers.get(i).getId());
                    }
                }
            }
            for (int j = 0; j < id.size(); j++) {
                flag = true;
                for (int i = 0; i < drivers.size(); i++) {
                    if (flag) {
                        if (drivers.get(i).getId() == id.get(j)) {
                            drivers.remove(drivers.get(i));
                            flag = false;
                        }
                    }
                }
            }
            if (drivers.size() > 0) {
                driver = Collections.min(drivers, Comparator.comparing(s -> s.getWorkingHour()));
            }
        }
        return driver;
    }
}
