package fmalc.api.service.impl;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.DriverStatusEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    DeliveryDetailService deliveryDetailService;

    private static int priorityPlace = 1;

    @Override
    public List<ScheduleForLocationDTO> getScheduleByConsignmentId(int id) {


        //-----------------------------------------------------------------------
        List<Schedule> schedules = scheduleRepository.findByConsignment_Id(id);
        List<ScheduleForLocationDTO> scheduleForLocationDTOs = new ArrayList<>();
        for (int i = 0; i < schedules.size(); i++) {
            ScheduleForLocationDTO scheduleForLocationDTO = convertScheduleResponse(schedules.get(i));
            scheduleForLocationDTO.setVehicle_id(schedules.get(i).getVehicle().getId());
            scheduleForLocationDTO.setDriver_id(schedules.get(i).getDriver().getId());
            scheduleForLocationDTOs.add(scheduleForLocationDTO);
        }
//
//         = mapToListResponse(schedules);
//

        return scheduleForLocationDTOs;
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {


        return scheduleRepository.save(schedule);
    }

    @Override
    public Vehicle findVehicleForSchedule(Consignment consignment) {
        boolean flag = true;
        List<Vehicle> vehiclesAvailable = vehicleService.findByStatus(VehicleStatusEnum.AVAILABLE.getValue());
        List<Vehicle> vehiclesMaintain = vehicleService.findByStatus(VehicleStatusEnum.MAINTAINING.getValue());
        Vehicle vehicle = new Vehicle();
        List<Integer> id = new ArrayList<>();
        MaintainCheckDTO maintainCheckDTO = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if (vehiclesAvailable.size() > 0) {
            for (int i = 0; i < vehiclesAvailable.size(); i++) {
                flag = true;
                double weight = consignment.getWeight();

//                if(vehiclesAvailable.get(i).getWeight())
                //check xe co lich bao tri trong tuong lai
                maintainCheckDTO = maintainanceService.checkMaintainForVehicle(vehiclesAvailable.get(i).getId());
                if (maintainCheckDTO.getId() != null && weight <= vehiclesAvailable.get(i).getWeight()) {
                    //
                    if (sdf.format(maintainCheckDTO.getMaintainDate()).compareTo(sdf.format(new Date())) >= 1) {
                        // check xem lich bao tri co trung ngay vs ngay giao hang khong
                        Place deliveryDetail =
                                deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
                        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
                        placeResponeDTO = placeResponeDTO.convertPlace(deliveryDetail);


                        String datePlace = sdf.format(placeResponeDTO.getPlannedTime());
                        String dateMaintain = sdf.format(maintainCheckDTO.getMaintainDate());

                        // ngay di lay hang sau ngay maintain
                        if (dateMaintain.compareTo(datePlace) <= -1) {
                            //
                            flag = false;
                        }
                        // ngay lay hang trc ngay maintain lon hon 1 ngay
                        else if (dateMaintain.compareTo(datePlace) > 1) {
                            Place placeReceive =
                                    deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.DELIVERED_PLACE.getValue());
                            String dateReceice = sdf.format(placeReceive.getPlannedTime());
                            // ngay tra hang trc ngay maintain
                            if (dateMaintain.compareTo(dateReceice) < -1) {
                                //
                                flag = false;
                            }// ngay lay hang trc ngay maintain, ngay giao hang sau ngay maintain ( lich maintain dc len trc)
                            else {
//                                vehiclesAvailable.remove(vehiclesAvailable.get(i));
                                id.add(vehiclesAvailable.get(i).getId());
                            }
                        }
                        if (flag) {
//                            vehiclesAvailable.remove(vehiclesAvailable.get(i));
                            id.add(vehiclesAvailable.get(i).getId());
                        }

                    }
                }else if(maintainCheckDTO.getId() != null && weight > vehiclesAvailable.get(i).getWeight() && flag){
                    id.add(vehiclesAvailable.get(i).getId());
                }else if(maintainCheckDTO.getId() == null && weight > vehiclesAvailable.get(i).getWeight() && flag){
                    id.add(vehiclesAvailable.get(i).getId());
                }else if(weight > vehiclesAvailable.get(i).getWeight()){
                    id.add(vehiclesAvailable.get(i).getId());
                }

            }
            for (int j = 0; j < id.size(); j++) {
                flag = true;
                for (int i = 0; i < vehiclesAvailable.size(); i++) {
                    if(flag){
                        if (vehiclesAvailable.get(i).getId() == id.get(j)) {
                            vehiclesAvailable.remove(vehiclesAvailable.get(i));
                            flag = false;
                        }
                    }

                }

            }
            if(vehiclesAvailable.size()>0){
                vehicle = vehicleService.getVehicleByKmRunning(vehiclesAvailable);
            }

        }
        return vehicle;
    }

    private ScheduleForLocationDTO convertScheduleResponse(Schedule schedule) {
        ModelMapper modelMapper = new ModelMapper();
        ScheduleForLocationDTO dto = modelMapper.map(schedule, ScheduleForLocationDTO.class);

        return dto;
    }

    public List<ScheduleForLocationDTO> mapToListResponse(List<Schedule> schedule) {
        return schedule.stream()
                .map(x -> convertScheduleResponse(x))
                .collect(Collectors.toList());
    }


    ////////////////


    public Driver findDriverForSchedule(Vehicle vehicle, Consignment consignment)  {
        VehicleReponseDTO vehicleReponseDTO = new VehicleReponseDTO();
        vehicleReponseDTO = vehicleReponseDTO.convertVehicle(vehicle);
        Driver driver = new Driver();
        List<Driver> drivers = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        boolean flag ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();
        drivers = driverService.getListDriverByLicense(vehicleReponseDTO.getWeight(), DriverStatusEnum.AVAILABLE.getValue());
        if(drivers.size() >0){
           for(int i=0; i<drivers.size();i++){
               flag = true;
               maintainCheckDTO = maintainanceService.checkMaintaiinForDriver(drivers.get(i).getId());
               if(maintainCheckDTO.getId() != null){
                   if (sdf.format(maintainCheckDTO.getMaintainDate()).compareTo(sdf.format(new Date())) >= 1) {
                       // check xem lich bao tri co trung ngay vs ngay giao hang khong
                       Place deliveryDetail =
                               deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
                       PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
                       placeResponeDTO = placeResponeDTO.convertPlace(deliveryDetail);


                       String datePlace = sdf.format(placeResponeDTO.getPlannedTime());
                       String dateMaintain = sdf.format(maintainCheckDTO.getMaintainDate());

                       // ngay di lay hang sau ngay maintain
                       if (dateMaintain.compareTo(datePlace) <= -1) {
                           //
                           flag = false;
                       }
                       // ngay lay hang trc ngay maintain lon hon 1 ngay
                       else if (dateMaintain.compareTo(datePlace) > 1) {
                           Place placeReceive =
                                   deliveryDetailService.getDeliveryByConsignmentAndPriority(consignment.getId(), priorityPlace, TypeLocationEnum.DELIVERED_PLACE.getValue());
                           String dateReceice = sdf.format(placeReceive.getPlannedTime());
                           // ngay tra hang trc ngay maintain
                           if (dateMaintain.compareTo(dateReceice) < -1) {
                               //
                               flag = false;
                           }// ngay lay hang trc ngay maintain, ngay giao hang sau ngay maintain ( lich maintain dc len trc)
                           else {
//                                vehiclesAvailable.remove(vehiclesAvailable.get(i));
                               id.add(drivers.get(i).getId());
                           }
                       }
                       if (flag) {
//                            vehiclesAvailable.remove(vehiclesAvailable.get(i));
                           id.add(drivers.get(i).getId());
                       }

                   }
               }

           }
            for (int j = 0; j < id.size(); j++) {
                flag = true;
                for (int i = 0; i < drivers.size(); i++) {
                    if(flag){
                        if (drivers.get(i).getId() == id.get(j)) {
                            drivers.remove(drivers.get(i));
                            flag = false;
                        }
                    }

                }

            }
            if(drivers.size()>0){
                driver = Collections.min(drivers, Comparator.comparing(s -> s.getWorkingHour()));
            }

        }
        return driver;
//        List<Consignment> consignments = consignmentService.getAllByStatus(0);
    }

}
