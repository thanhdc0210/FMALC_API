package fmalc.api.service.impl;

import fmalc.api.dto.*;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Place;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.ScheduleConsginmentEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.MaintenanceService;
import fmalc.api.service.PlaceService;
import fmalc.api.service.ScheduleService;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PlaceService placeService;

    @Autowired
    MaintenanceService maintainanceService;
    private static int priorityPlace = 1;

    @Override
    public String findVehicleLicensePlatesForReportInspectionBeforeDelivery(String username, Timestamp endDate, Integer status) {
        return vehicleRepository.findVehicleLicensePlatesForReportInspectionBeforeDelivery(username, endDate, status);
    }

    @Override
    public String findVehicleLicensePlatesForReportInspectionAfterDelivery(String username, Timestamp startDate, Integer status, Integer type) {
        return vehicleRepository.findVehicleLicensePlatesForReportInspectionAfterDelivery(username, startDate, status, type);
    }

    @Override
    public Vehicle updateKmVehicle(int id, int km) {
        Vehicle vehicle = vehicleRepository.findById(id).get();
        vehicle.setKilometerRunning(km);
        vehicle = vehicleRepository.save(vehicle);
        return vehicle;
    }

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        vehicle = vehicleRepository.saveAndFlush(vehicle);
        maintainanceService.createFirstMaintain(vehicle);
        return vehicle;
    }

    @Override
    public VehicleForDetailDTO findVehicleById(int id) {
        Vehicle vehicle = vehicleRepository.findById(id).get();
        VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
        vehicleForDetailDTO = vehicleForDetailDTO.convertToDto(vehicle);
        return vehicleForDetailDTO;
    }

    @Override
    public Vehicle findVehicleByIdForLocation(int id) {
        return vehicleRepository.findById(id).get();
    }

    @Override
    public List<Vehicle> getListVehicle() {
        return vehicleRepository.getListVehicle(true);
    }

    @Override
    public Vehicle disableVehicle(int id) {
        Vehicle vehicle = vehicleRepository.findByIdVehicle(id);
        vehicle.setIsActive(false);

        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle findVehicleByLicensePlates(String licensePlates) {
        return vehicleRepository.findByLicensePlates(licensePlates);
    }

    @Override
    public List<Vehicle> findByStatus(int status, double weight) {

        return vehicleRepository.findByStatus(status, weight);
    }

    @Override
    public List<Vehicle> findByWeight(double weight) {
        List<Vehicle> vehicles = vehicleRepository.findByWeight(weight, true);
        return vehicles.stream()
                .filter(x -> x.getStatus() != VehicleStatusEnum.SOLD.getValue())
                .collect(Collectors.toList());
    }


    @Override
    public List<Vehicle> findByWeightBigger(double weight) {
        List<Vehicle> vehicles = vehicleRepository.findByWeightBigger(weight);
        List<Vehicle> result = new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getStatus() != VehicleStatusEnum.SOLD.getValue()) {
                result.add(vehicles.get(i));
            }
        }
        return result;
    }

    @Override
    public List<Vehicle> findByWeightSmaller(double weight) {
        List<Vehicle> vehicles = vehicleRepository.findByWeightSmaller(weight);
        List<Vehicle> result = new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getStatus() != VehicleStatusEnum.SOLD.getValue()) {
                result.add(vehicles.get(i));
            }
        }
        return result;
    }

       // find vehicles for schedule
    @Override
    public List<Vehicle> findVehicleForSchedule(Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO, int sche) {
        List<VehicleConsignmentDTO> vehicleConsignmentDTOS = consignmentRequestDTO.getVehicles();
        List<Vehicle> vehicles = new ArrayList<>();
        List<Vehicle> result = new ArrayList<>();
        for (int i = 0; i < vehicleConsignmentDTOS.size(); i++) {
            int size = Integer.parseInt(vehicleConsignmentDTOS.get(i).getQuantity());
//            if (size > 0) {
                double weight = Double.parseDouble(vehicleConsignmentDTOS.get(i).getWeight());
                vehicles = findByWeight(weight);
                int tmp=0;
                for(int t = 0 ; t< vehicles.size();t++){
                    tmp =i;
                    if(result.contains(vehicles.get(t))){
                       vehicles.remove(vehicles.get(t));
                       i=tmp;
                    }
                }
//                vehicles = checkDuplicate(result, vehicles);
                if (vehicles.size() > 0) {
                    vehicles = checkMaintainForVehicle(vehicles, consignment);
                    if (sche == ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue()) {
                        vehicles = checkScheduledForVehicle(vehicles, consignment);
                    }
                    if (vehicles.size() > 0) {
                        if (vehicles.size() >= size) {
                            result = checkDuplicate(result, vehicles);
                        } else {
                            List<Vehicle> vehicleSmaller = findByWeightSmaller(weight);
                            if (vehicleSmaller.size() > 0) {
                                vehicleSmaller = checkMaintainForVehicle(vehicleSmaller, consignment);
                                if (sche == ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue()) {
                                    vehicleSmaller = checkScheduledForVehicle(vehicleSmaller, consignment);
                                }

                                if (vehicleSmaller.size() > 0 && vehicleSmaller.size() >= (size - vehicles.size())) {
                                    vehicles.addAll(vehicleSmaller);
                                    result = checkDuplicate(result, vehicles);
                                } else {
                                    vehicles.addAll(vehicleSmaller);
                                    result = checkDuplicate(result, vehicles);
                                }
                            } else {
                            }
                        }
                    } else {
                        if (result.size() > 0) {
                            result = new ArrayList<>();
                        }
                        List<Vehicle> vehicleSmaller = findByWeightSmaller(weight);
                        if (vehicleSmaller.size() > 0) {
                            vehicleSmaller = checkMaintainForVehicle(vehicleSmaller, consignment);
                            if (sche == ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue()) {
                                vehicleSmaller = checkScheduledForVehicle(vehicleSmaller, consignment);
                            }
                            if (vehicleSmaller.size() > 0 && vehicleSmaller.size() >= (size - vehicles.size())) {
                                result = checkDuplicate(result, vehicles);
                            } else {
                                result = checkDuplicate(result, vehicles);
                            }
                        } else {
                        }
                    }

                } else {

                    List<Vehicle> vehicleSmaller = findByWeightSmaller(weight);
                    if (vehicleSmaller.size() > 0) {
                        vehicleSmaller = checkMaintainForVehicle(vehicleSmaller, consignment);
                        if (sche == ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue()) {
                            vehicleSmaller = checkScheduledForVehicle(vehicleSmaller, consignment);
                        }
                        if (vehicleSmaller.size() > 0 && vehicleSmaller.size() >= (size - vehicles.size())) {
//                            vehicles.addAll(vehicleSmaller);
                            result = checkDuplicate(result, vehicleSmaller);
                        } else {
//                            vehicles.addAll(vehicleSmaller);
                            result = checkDuplicate(result, vehicleSmaller);
                        }
                    } else {
                        //vehicleBigger size = 0
                    }
                }
//            }

        }
        Collections.sort(result, (Vehicle v1, Vehicle v2) -> v1.getWeight().compareTo(v2.getWeight()));
        return result;
    }

    @Override
    public List<ScheduleForConsignmentDTO> findScheduleForFuture(List<Vehicle> vehicles, Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO) {
        boolean flag = true;
        int flagInt;
        List<ScheduleForConsignmentDTO> scheduleForLocationDTOS = new ArrayList<>();
        List<Vehicle> result = new ArrayList<>();
        List<ScheduleForConsignmentDTO> scheduleResult = new ArrayList<>();

        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        for (int i = 0; i < vehicles.size(); i++) {
            flag = true;
            scheduleForLocationDTOS = checkScheduleForVehicle(vehicles.get(i).getId());
            if (scheduleForLocationDTOS.size() > 0) {
                for (int j = 0; j < scheduleForLocationDTOS.size(); j++) {

                    scheduleForLocationDTO = scheduleForLocationDTOS.get(j);
                    flagInt = checkVehicleSchedule(scheduleForLocationDTO, consignment, flag);
                    if (flagInt == 1) {
//                        for(int f = j; f<scheduleForLocationDTOS.size();f++){
//                            int flagTmp = checkVehicleSchedule(scheduleForLocationDTO, consignment, flag);
//                            if(flagTmp == -1)
//                        }
//                        scheduleResult.add(scheduleForLocationDTO);
                        flag = false;
                    } else if (flagInt == 0) {
//                        flag = true;
//                        j = scheduleForLocationDTOS.size();
                    } else if (flagInt == -1) {
                        j = scheduleForLocationDTOS.size();
                        flag = true;
                    }
                }
                if (!flag) {
                    int count  = 0;
                    int min = checkVehicleScheduleDeli(scheduleForLocationDTOS.get(0), consignment);
                    scheduleForLocationDTO = scheduleForLocationDTOS.get(0);

                    int max = checkVehicleScheduleDeli(scheduleForLocationDTOS.get(0), consignment);
                    ScheduleForConsignmentDTO scheduleForConsignmentDTOMAX = scheduleForLocationDTOS.get(0);

                    for (int j = 1; j < scheduleForLocationDTOS.size(); j++) {
                        int tmp = checkVehicleScheduleDeli(scheduleForLocationDTOS.get(j), consignment);
                        if (tmp > 0) {
                            if (min > tmp) {
                                min = tmp;
                                scheduleForLocationDTO = scheduleForLocationDTOS.get(j);
                            }else if(min <=0){
                                min = tmp;
                                scheduleForLocationDTO = scheduleForLocationDTOS.get(j);
                            }
                            count++;
                        }else if( tmp <0){

                            if (max < tmp) {
                                max = tmp;
                                scheduleForConsignmentDTOMAX = scheduleForLocationDTOS.get(j);
                            }else if(max <=0){
                                max = tmp;
                                scheduleForConsignmentDTOMAX = scheduleForLocationDTOS.get(j);
                            }

                        }
                    }
//                    if( ){
                        if (count >0 &&checkVehicleScheduleDeli(scheduleForLocationDTO,consignment)!=0) {
                            scheduleResult.add(scheduleForLocationDTO);
                        }
                        else if(count == 0 && checkVehicleScheduleDeli(scheduleForConsignmentDTOMAX,consignment)!=0 ){
                            scheduleResult.add(scheduleForConsignmentDTOMAX);
                        }
//                    }

//                    scheduleForLocationDTO.setVehicle_id(vehicles.get(i).getId());

                }

            }
        }

        return scheduleResult;
    }


    private int checkVehicleSchedule(ScheduleForConsignmentDTO scheduleForLocationDTO, Consignment consignment, boolean flag) {
        Long nowTime = new Date().getTime();
        long diff;
        int flagInt = 0;
//        flag = true;

        List<Place> places = (List<Place>) consignment.getPlaces();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<PlaceResponeDTO> listScheduleDeli =
                placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());
//
//                    //list place delivery of a consignment

        // Consignemnt
        PlaceResponeDTO placeConsignmentRecei =
                getPlaceByTypePlaceAndPriority(places, 1, TypeLocationEnum.RECEIVED_PLACE.getValue());

        PlaceResponeDTO placeScheduleDeli =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), listScheduleDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());
        PlaceResponeDTO placeScheduleRecei =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());

        if (placeConsignmentRecei.getAddress() != null && placeScheduleDeli.getAddress() != null) {
//            if(sdf.format(placeConsignmentRecei.getPlannedTime()).compareTo(sdf.format(placeScheduleRecei.getPlannedTime())) ==0){
                diff = placeConsignmentRecei.getPlannedTime().getTime() - placeScheduleDeli.getPlannedTime().getTime();
                int diffDays = (int) diff / (24 * 60 * 60 * 1000);
                int diffHours = (int) diff / (60 * 60 * 1000) % 24;
                if (diffDays == 0) {
                    if (diffHours >= 1) {
                        flagInt = 1;
//                    flag = false;
                    } else if (diffHours <= -1) {

                        List<PlaceResponeDTO> listConsignmentDeli =
                                getPlaceByTypePlace(places, TypeLocationEnum.DELIVERED_PLACE.getValue());

                        PlaceResponeDTO placeConsignmentDeli =
                                getPlaceByTypePlaceAndPriority(places, listConsignmentDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());
                        if (placeConsignmentDeli.getAddress() != null && placeScheduleRecei.getAddress() != null) {
                            diff = placeScheduleRecei.getPlannedTime().getTime() - placeConsignmentDeli.getPlannedTime().getTime();
                            diffDays = (int) diff / (24 * 60 * 60 * 1000);
                            diffHours = (int) diff / (60 * 60 * 1000) % 24;
                            if (diffDays == 0) {
                                if (diffHours >= 1) {
//                                flag = false;
                                    flagInt = 1;
                                } else {
                                    flagInt = -1;
//                                flag = true;
                                }
                            } else {

                            }
                        }
                    } else {
                        flagInt = -1; //trung'
                    }
                } else {
                    flagInt = 0;
                }
//            }

        }
        return flagInt;

    }

    private int checkVehicleScheduleDeli(ScheduleForConsignmentDTO scheduleForLocationDTO, Consignment consignment) {
        long diff;
        int result = 0;
        List<Place> places = (List<Place>) consignment.getPlaces();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<PlaceResponeDTO> listScheduleDeli =
                placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());
        PlaceResponeDTO placeConsignmentRecei =
                getPlaceByTypePlaceAndPriority(places, 1, TypeLocationEnum.RECEIVED_PLACE.getValue());

        PlaceResponeDTO placeScheduleDeli =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), listScheduleDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

        PlaceResponeDTO placeScheduleRcei =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());


        if (placeScheduleDeli.getAddress() != null && placeConsignmentRecei.getAddress() != null) {
            diff = placeConsignmentRecei.getPlannedTime().getTime() - placeScheduleDeli.getPlannedTime().getTime();
            if(sdf.format(placeConsignmentRecei.getPlannedTime()).compareTo(sdf.format(placeScheduleRcei.getPlannedTime()))==0){
                int diffHours = (int) diff / (60 * 60 * 1000) % 24;

                if (diffHours >= 1 ) {
                    result = diffHours;
                }else if(diffHours<=-1){
                    result = diffHours;
                }
            }else if(sdf.format(placeConsignmentRecei.getPlannedTime()).compareTo(sdf.format(placeScheduleRcei.getPlannedTime()))>0){

            }


        }
        return result;

    }
    private List<Vehicle> checkMaintainForVehicle(List<Vehicle> vehicles, Consignment consignment) {

        boolean flag = true;
        List<MaintainCheckDTO> maintainCheckDTO = new ArrayList<>();
        List<Vehicle> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        for (int i = 0; i < vehicles.size(); i++) {
            flag = true;
//            maintainCheckDTO = new MaintainCheckDTO();
//            double weight = consignment.getWeight();
//            VehicleForDetailDTO vehicle = vehicleService.findVehicleById(vehicles.get(i).getId());
            //check xe co lich bao tri trong tuong lai
            maintainCheckDTO = maintainanceService.checkMaintainForVehicle(vehicles.get(i).getId());
            for(int m =0; m< maintainCheckDTO.size(); m++){
                if (maintainCheckDTO.get(m).getId() != null) {


                    //list place receive of a consignment
                    flag = checkDateMaintain(consignment, maintainCheckDTO.get(m), flag);
                    if (flag) {
//                        result.add(vehicles.get(i));
                        m = maintainCheckDTO.size();
                    }else{
                        flag = false;

                    }

                }
            }

            if (!flag) {
                result.add(vehicles.get(i));
            }
        }

        return result;
    }

    private PlaceResponeDTO getPlaceByTypePlaceAndPriority(List<Place> places, int priority, int type){
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        Place place = new Place();
        for(int i = 0; i< places.size(); i++){
            if(places.get(i).getPriority() == priority && places.get(i).getType() == type){
                place = places.get(i);
            }
        }
        if(place!= null){
            placeResponeDTO = placeResponeDTO.convertPlace(place);
        }

        return  placeResponeDTO;
    }

    private List<PlaceResponeDTO> getPlaceByTypePlace(List<Place> places,  int type){
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        List<PlaceResponeDTO> placeResponeDTOS = new ArrayList<>();
        List<Place> placesResult = new ArrayList<>();
        for(int i = 0; i< places.size(); i++){
            if(places.get(i).getType() == type){
                placesResult.add(places.get(i));
            }
        }
        if(places!= null){
            placeResponeDTOS = placeResponeDTO.mapToListResponse(placesResult);
        }

        return  placeResponeDTOS;
    }

    private boolean checkDateMaintain(Consignment consignment, MaintainCheckDTO maintainCheckDTO, boolean flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<Place> places = new ArrayList<>();
        places = (List<Place>) consignment.getPlaces();
        PlaceResponeDTO placeSchedulePriorityRecei =   getPlaceByTypePlaceAndPriority(places, priorityPlace, TypeLocationEnum.DELIVERED_PLACE.getValue());

        //list place delivery of a consignment
        List<PlaceResponeDTO> placeConsgimentsPriorityDeli =
               getPlaceByTypePlace(places, TypeLocationEnum.DELIVERED_PLACE.getValue());

        //receive and priority =1
        if (placeSchedulePriorityRecei != null) {
            String dateReceiConsignment = sdf.format(placeSchedulePriorityRecei.getPlannedTime());
            String dateMaintain = sdf.format(maintainCheckDTO.getPlannedMaintainDate());
            if (dateReceiConsignment.compareTo(dateMaintain) <= 1) {
                PlaceResponeDTO placeSchedulePriorityDeli =
                        getPlaceByTypePlaceAndPriority(places, placeConsgimentsPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());
                if (placeConsgimentsPriorityDeli != null) {
                    String dateConsignemntDeli = sdf.format(placeSchedulePriorityDeli.getPlannedTime());
                    if (dateConsignemntDeli.compareTo(dateMaintain) <= 1) {
                        flag = false;
                    }
                }

            } else if (dateReceiConsignment.compareTo(dateMaintain) >= 1) {
                flag = false;
            }
        }
        return flag;
    }
    @Override
    public List<Vehicle> checkScheduledForVehicle(List<Vehicle> vehicles, Consignment consignment) {
        boolean flag = true;
        List<ScheduleForConsignmentDTO> scheduleForLocationDTOS = new ArrayList<>();
        List<Vehicle> result = new ArrayList<>();


        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        for (int i = 0; i < vehicles.size(); i++) {
            flag = true;

            scheduleForLocationDTOS = checkScheduleForVehicle(vehicles.get(i).getId());
            if (scheduleForLocationDTOS.size() > 0) {
                for (int j = 0; j < scheduleForLocationDTOS.size(); j++) {

                    scheduleForLocationDTO = scheduleForLocationDTOS.get(j);
                    flag = checkDateConsignmentAndSchedule(scheduleForLocationDTO, consignment, flag);

                    if (flag) {
                        j = scheduleForLocationDTOS.size();
                    }
                }
                if (!flag) {
                    result.add(vehicles.get(i));

                }
            } else {
                result.add(vehicles.get(i));
            }

        }
        return result;
    }

    @Override
    public List<ScheduleForConsignmentDTO> checkScheduleForVehicle(int idVehicle) {
        List<ScheduleForConsignmentDTO> scheduleForLocationDTOS = new ArrayList<>();
        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        List<ScheduleForConsignmentDTO> result = new ArrayList<>();
        boolean flag;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        List<Schedule> Schedules = scheduleService.checkVehicleInScheduled(idVehicle);
        if (Schedules.size() > 0) {
            scheduleForLocationDTOS = scheduleForLocationDTO.mapToListResponse(Schedules);
            if (scheduleForLocationDTOS.size() > 0) {
                for (int i = 0; i < scheduleForLocationDTOS.size(); i++) {
                    flag = true;
                    List<PlaceResponeDTO> listDeli = placeService.getPlaceByTypePlace(scheduleForLocationDTOS.get(i).getConsignment().getId(),TypeLocationEnum.DELIVERED_PLACE.getValue());
                    PlaceResponeDTO deliveryDetail = placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTOS.get(i).getConsignment().getId(), listDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());
                    if (deliveryDetail.getPlannedTime() != null) {
                        String datePlace = sdf.format(deliveryDetail.getPlannedTime());
                        String dateNow = sdf.format(new Date());
                        if (dateNow.compareTo(datePlace) <= 0) {
                            flag = false;
//                            id.add(scheduleForLocationDTOS.get(i).getId());
                        }
                    } else {
                        flag = false;
//                        id.add(scheduleForLocationDTOS.get(i).getId());
                    }
                    if (flag) {
                        result.add(scheduleForLocationDTOS.get(i));
                    }
                }

            }
        }
        return scheduleForLocationDTOS;
    }

    private List<Vehicle> checkDuplicate(List<Vehicle> result, List<Vehicle> vehicles) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (!result.contains(vehicles.get(i))) {
                result.add(vehicles.get(i));
            }
        }
        return result;
    }

    private boolean checkDateConsignmentAndSchedule(ScheduleForConsignmentDTO scheduleForLocationDTO, Consignment consignment, boolean flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        flag = true;
        List<Place>  places = (List<Place>) consignment.getPlaces();
        List<PlaceResponeDTO> listScheduleDeli =
                placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());
//
//                    //list place delivery of a consignment
        List<PlaceResponeDTO> listConsginmentDeli =
                getPlaceByTypePlace(places, TypeLocationEnum.DELIVERED_PLACE.getValue());

        // Consignemnt
        PlaceResponeDTO placeConsignmentRecei =
                getPlaceByTypePlaceAndPriority(places, 1, TypeLocationEnum.RECEIVED_PLACE.getValue());

        PlaceResponeDTO placeConsignmentDeli =
                getPlaceByTypePlaceAndPriority(places, listConsginmentDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

        // Schedule
        // lấy thời gian consignment  lấy hàng có độ ưu tiên 1
        PlaceResponeDTO placeScheduleRecei =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());
        // lấy thằng thời gian giao hàng sau cùng
        PlaceResponeDTO placeScheduleDeli =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), listScheduleDeli.size(), TypeLocationEnum.RECEIVED_PLACE.getValue());

        if (placeConsignmentRecei.getAddress() != null && placeScheduleRecei.getAddress() != null) {
            String dateReceiConsignment = sdf.format(placeConsignmentRecei.getPlannedTime());
            String dateScheduleRecei = sdf.format(placeScheduleRecei.getPlannedTime());
            if (dateReceiConsignment.compareTo(dateScheduleRecei) >= 1) {
                if (placeScheduleDeli.getAddress() != null) {
                    String dateScheduleDe = sdf.format(placeScheduleDeli.getPlannedTime());
                    if (dateReceiConsignment.compareTo(dateScheduleDe) >= 1) {
                        flag = false;
                    }
                }
            } else if (dateReceiConsignment.compareTo(dateScheduleRecei) <= -1) {
                if (placeConsignmentDeli != null) {
                    String dateConsignmentDeli = sdf.format(placeConsignmentDeli.getPlannedTime());
                    if (dateConsignmentDeli.compareTo(dateScheduleRecei) <= -1) {
                        flag = false;
                        // xe pass 1 schedule
                    }
                }

            }

        }
        return flag;

    }

    @Override
    public int updateStatus(int status, int id) {
//       Vehicle vehicle= vehicleRepository.updateStatusVehicle(status, id);
       return  vehicleRepository.updateStatusVehicle(status, id);
    }


    public Vehicle findVehicleByUsernameAndConsignmentStatus(String username, List<Integer> status,
                                                             Timestamp startDate, Timestamp endDate) {
        return vehicleRepository.findVehicleByUsernameAndConsignmentStatus(username, status, startDate, endDate);
    }
}
