package fmalc.api.controller;


import fmalc.api.dto.*;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Driver;

import fmalc.api.dto.DetailedScheduleDTO;
import fmalc.api.dto.ScheduleResponseDTO;

import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.ScheduleConsginmentEnum;
import fmalc.api.enums.SearchTypeForDriverEnum;
import fmalc.api.service.ConsignmentService;
import fmalc.api.service.DriverService;
import fmalc.api.service.ScheduleService;
import fmalc.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/schedules")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    ConsignmentService consignmentService;

    @GetMapping(value = "driver")

    public ResponseEntity<List<ScheduleResponseDTO>> findByConsignmentStatusAndUsername(@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username){
        List<Schedule> schedules = scheduleService.findByConsignmentStatusAndUsername(status, username);


        if (schedules == null) {
            return ResponseEntity.noContent().build();
        }
        List<ScheduleResponseDTO> consignmentResponses = new ArrayList<>(new ScheduleResponseDTO().mapToListResponse(schedules));

        return ResponseEntity.ok().body(consignmentResponses);
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<DetailedScheduleDTO> findById(@PathVariable("id") Integer id) {
        Schedule schedule = scheduleService.findById(id);
        if (schedule == null || schedule.equals("")) {
            return ResponseEntity.noContent().build();
        }
        DetailedScheduleDTO detailedScheduleDTO = new DetailedScheduleDTO(schedule);

        return ResponseEntity.ok().body(detailedScheduleDTO);
    }

    @PostMapping
    public ResponseEntity<ScheduleToConfirmDTO> createSchedule(@RequestBody RequestObjectDTO requestObjectDTO) throws ParseException {
        ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        if(requestObjectDTO.getSchedule().size()>0){
            scheduleToConfirmDTO =schedulesConfirm(requestObjectDTO);
        }else{
            List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
            Consignment consignment = consignmentResponseDTO.mapToEntity(requestObjectDTO.getNewConsignment());
            ConsignmentRequestDTO consignmentRequestDTO = requestObjectDTO.getConsignmentRequest();
            scheduleToConfirmDTO= scheduleReturn(consignment, consignmentRequestDTO,scheduleForConsignmentDTOS);
        }


//        scheduleForConsignment
            return ResponseEntity.ok().body(scheduleToConfirmDTO);
    }

    private ScheduleToConfirmDTO schedulesConfirm(RequestObjectDTO requestObjectDTO) throws ParseException {
        List<ScheduleToConfirmDTO> scheduleToConfirmDTOS = new ArrayList<>();
        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
        List<ObejctScheDTO> obejctScheDTOS = new ArrayList<>();
        Schedule schedule = new Schedule();
        List<Schedule> schedules = new ArrayList<>();
        DriverForScheduleDTO driverForScheduleDTO = new DriverForScheduleDTO();
        obejctScheDTOS = requestObjectDTO.getSchedule();
        ConsignmentRequestDTO consignmentRequestDTO = requestObjectDTO.getConsignmentRequest();
        ConsignmentRequestDTO consignmentRe = consignmentRequestDTO;
        VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
        List<VehicleConsignmentDTO> vehicleConsignmentDTOS = consignmentRequestDTO.getVehicles();
        vehicleConsignmentDTOS.sort((VehicleConsignmentDTO v1, VehicleConsignmentDTO v2) -> (v1.getWeight().compareTo(v2.getWeight())));
        Consignment consignment = consignmentResponseDTO.mapToEntity(requestObjectDTO.getNewConsignment());
        List<ObejctScheDTO> removeObject = new ArrayList<>();
        int total = 0;
        for (int obj = 0; obj < vehicleConsignmentDTOS.size(); obj++) {
            total += Integer.parseInt(vehicleConsignmentDTOS.get(obj).getQuantity());
        }
        for (int i = 0; i < vehicleConsignmentDTOS.size(); i++) {
            double weight = Double.parseDouble(vehicleConsignmentDTOS.get(i).getWeight());
            int quan = Integer.parseInt(vehicleConsignmentDTOS.get(i).getQuantity());
            if (removeObject.size() > 0) {
                removeObject = new ArrayList<>();
            }
            for (int j = 0; j < obejctScheDTOS.size(); j++) {
//                int count = 1;
                schedule = scheduleService.findScheduleByVehDriCon(obejctScheDTOS.get(j));
                if (schedule.getVehicle().getWeight() == weight && quan > 0) {
                    schedules.add(schedule);
                    quan = quan - 1;
                    removeObject.add(obejctScheDTOS.get(j));
                }
                if (quan == 0 && removeObject.size() < obejctScheDTOS.size()) {
                    j = obejctScheDTOS.size();
                } else if (quan == 0 && obejctScheDTOS.size() == removeObject.size()) {
                    j = obejctScheDTOS.size();
                } else if (quan > 0 && obejctScheDTOS.size() == removeObject.size()) {
                    j = obejctScheDTOS.size();
                }
            }
            vehicleConsignmentDTOS.get(i).setQuantity(String.valueOf(quan));

            if(quan>0){
                for (int j = 0; j < obejctScheDTOS.size(); j++) {
//                int count = 1;
                    schedule = scheduleService.findScheduleByVehDriCon(obejctScheDTOS.get(j));
                    if (schedule.getVehicle().getWeight() > weight && quan > 0) {
                        schedules.add(schedule);
                        quan = quan - 1;
                        removeObject.add(obejctScheDTOS.get(j));
                    }
                    if (quan == 0 && removeObject.size() < obejctScheDTOS.size()) {
                        j = obejctScheDTOS.size();
                    } else if (quan == 0 && obejctScheDTOS.size() == removeObject.size()) {
                        j = obejctScheDTOS.size();
                    } else if (quan > 0 && obejctScheDTOS.size() == removeObject.size()) {
                        j = obejctScheDTOS.size();
                    }
                }
                vehicleConsignmentDTOS.get(i).setQuantity(String.valueOf(quan));
            }

            if (schedules.size() == obejctScheDTOS.size()) {
//                if (i < vehicleConsignmentDTOS.size() - 1) {
//                    consignmentRequestDTO.setVehicles(vehicleConsignmentDTOS);
////                    List<Vehicle> vehicles =
////                            scheduleService.findVehicleForSchedule(consignment, consignmentRequestDTO);
//                    scheduleToConfirmDTOS = scheduleReturn( consignment, consignmentRequestDTO);
                i = vehicleConsignmentDTOS.size();
//                }
            }
            obejctScheDTOS.removeAll(removeObject);
//            if(vehicleConsignmentDTOS.get(i).getWeight() >= )
        }

            List<Vehicle> vehicles = new ArrayList<>();
            List<Driver> drivers = new ArrayList<>();
            List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
            consignmentRequestDTO.setVehicles(vehicleConsignmentDTOS);
            if (total <= schedules.size()) {
                for (int sch = 0; sch < total; sch++) {
                    List<Driver>  resultDriver = driverService.findDriverForSchedule(schedules.get(sch).getVehicle().getWeight(), consignment);
                    schedule = new Schedule();
                    schedule.setVehicle(schedules.get(sch).getVehicle());
                    schedule.setDriver(schedules.get(sch).getDriver());
                    schedule.setIsApprove(false);
                    schedule.setConsignment(consignment);
                    schedule.setImageConsignment(consignmentRequestDTO.getImageConsignment());
                    schedule.setNote("");
//                    schedule =  scheduleService.createSchedule(schedule);
                    if (schedule != null) {
                        scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
                    }
                }
                vehicles = vehicleService.findVehicleForSchedule(consignment, consignmentRequestDTO,ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue());
                for (int ve = 0; ve < vehicles.size(); ve++) {
                    List<Driver> driverList = new ArrayList<>();
                    driverList = driverService.findDriverForSchedule(vehicles.get(ve).getWeight(), consignment);
                    for (int dri = 0; dri < driverList.size(); dri++) {
                        if (!drivers.contains(driverList.get(dri))) {
                            drivers.add(driverList.get(dri));
                        }
                    }
//                                driverList.removeIf(driver -> {drivers.contains(driver)});
                }
                scheduleToConfirmDTO.setScheduleForConsignmentDTOS(scheduleForConsignmentDTOS);
                scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
                scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTO.mapToListResponse(vehicles));

            } else if (total >= schedules.size()) {
                for (int sch = 0; sch < schedules.size(); sch++) {
                    schedule = new Schedule();
                    schedule.setVehicle(schedules.get(sch).getVehicle());
                    schedule.setDriver(schedules.get(sch).getDriver());
                    schedule.setIsApprove(false);
                    schedule.setConsignment(consignment);
                    schedule.setImageConsignment(consignmentRequestDTO.getImageConsignment());
                    schedule.setNote("");
//                    schedule = scheduleService.createSchedule(schedule);
                    if (schedule != null) {
                        scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
                    }
                }
                vehicles = vehicleService.findVehicleForSchedule(consignment, consignmentRequestDTO,ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue());
                for (int ve = 0; ve < vehicles.size(); ve++) {
                    List<Driver> driverList = new ArrayList<>();
                    driverList = driverService.findDriverForSchedule(vehicles.get(ve).getWeight(), consignment);
                    for (int dri = 0; dri < driverList.size(); dri++) {
                        if (!drivers.contains(driverList.get(dri))) {
                            drivers.add(driverList.get(dri));
                        }
                    }
//                                driverList.removeIf(driver -> {drivers.contains(driver)});
                }


                scheduleToConfirmDTO.getScheduleForConsignmentDTOS().addAll(scheduleForConsignmentDTOS);
                scheduleToConfirmDTO.getDriverForScheduleDTOS().addAll(driverForScheduleDTO.mapToListResponse(drivers));
                scheduleToConfirmDTO.getVehicleForDetailDTOS().addAll(vehicleForDetailDTO.mapToListResponse(vehicles));
                if (total > 0) {
                    ScheduleToConfirmDTO resultReturn = scheduleReturn(consignment, consignmentRequestDTO, scheduleToConfirmDTO.getScheduleForConsignmentDTOS());
                    if (resultReturn.getScheduleForConsignmentDTOS().size() > 0 ) {

                        scheduleToConfirmDTO.getScheduleForConsignmentDTOS().addAll(resultReturn.getScheduleForConsignmentDTOS());
                        scheduleToConfirmDTO.getDriverForScheduleDTOS().addAll(resultReturn.getDriverForScheduleDTOS());
                        scheduleToConfirmDTO.getVehicleForDetailDTOS().addAll(resultReturn.getVehicleForDetailDTOS());

                    }
                    if( resultReturn.getScheduleForConsignmentDTOS().size() == total){
                        scheduleToConfirmDTO.setQuantity(0);
                    }else if(resultReturn.getScheduleForConsignmentDTOS().size() < total){
                        scheduleToConfirmDTO.setQuantity(total-resultReturn.getScheduleForConsignmentDTOS().size());
                    }
                }

            }


        return  scheduleToConfirmDTO;
    }

    private ScheduleToConfirmDTO scheduleReturn(Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO, List<ScheduleForConsignmentDTO> scheduleds) throws ParseException {
        List<Vehicle> vehicles =
                vehicleService.findVehicleForSchedule(consignment, consignmentRequestDTO, ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue());
        int sizeVehicle = 0;
        for (int i = 0; i < consignmentRequestDTO.getVehicles().size(); i++) {
            String quantity = consignmentRequestDTO.getVehicles().get(i).getQuantity();
            sizeVehicle += Integer.parseInt(quantity);
        }
        List<Vehicle> vehiclesSave = new ArrayList<>();
        List<Driver> driversSave = new ArrayList<>();
        List<ScheduleToConfirmDTO> scheduleToConfirmDTOS = new ArrayList<>();
        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
        ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
        Schedule schedule = new Schedule();
        List<VehicleForDetailDTO> vehicleForDetailDTOS = new ArrayList<>();
        VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
        List<Driver> drivers = new ArrayList<>();
        if (vehicles.size() > 0 && vehicles.size() >= sizeVehicle) {
            Collections.sort(vehicles, new Comparator<Vehicle>() {
                @Override
                public int compare(Vehicle o1, Vehicle o2) {
                    return o1.getKilometerRunning().compareTo(o2.getKilometerRunning());
                }
            });

            Collections.sort(vehicles, new Comparator<Vehicle>() {
                @Override
                public int compare(Vehicle o1, Vehicle o2) {
                    return o1.getWeight().compareTo(o2.getWeight());
                }
            });
            for (int i = 0; i < sizeVehicle; i++) {
                if (scheduleds.size() > 0) {
                    for (int s = 0; s < scheduleds.size(); s++) {
                        if (vehicles.get(i).getId() != scheduleds.get(s).getVehicle().getId() && !vehiclesSave.contains(vehicles.get(i))) {
                            vehiclesSave.add(vehicles.get(i));
                        }
                    }

                }else{
                    if ( !vehiclesSave.contains(vehicles.get(i))) {
                        vehiclesSave.add(vehicles.get(i));
                    }

                }

            }
//            scheduleToConfirmDTO.setVehicles(vehicleForDetailDTO.mapToListResponse(vehiclesSave));
            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTO.mapToListResponse(vehicles));
        } else if (vehicles.size() > 0 && vehicles.size() < sizeVehicle) {
            vehicleForDetailDTOS = vehicleForDetailDTO.mapToListResponse(vehicles);
            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTOS);

            // thieu xe
        }

///////////////////////////////////////////////////////////////////////
        List<Driver> resultDriver = new ArrayList<>();
        DriverForScheduleDTO driverForScheduleDTO = new DriverForScheduleDTO();
        if (vehicles.size() > 0 && vehicles.size() >= sizeVehicle) {
            for (int i = 0; i < consignmentRequestDTO.getVehicles().size(); i++) {
                double weight = Double.parseDouble(consignmentRequestDTO.getVehicles().get(i).getWeight());
                int quantity = Integer.parseInt(consignmentRequestDTO.getVehicles().get(i).getQuantity());
                if (quantity > 0) {
                    resultDriver = driverService.findDriverForSchedule(weight, consignment);
                    for (int j = 0; j < resultDriver.size(); j++) {
                        if (scheduleds.size() > 0) {
                            for (int s = 0; s < scheduleds.size(); s++) {
                                if (resultDriver.get(j).getId() != scheduleds.get(s).getDriver().getId() && !drivers.contains(resultDriver.get(j))) {
                                    drivers.add(resultDriver.get(j));
                                }
                            }

                        }else{
                            if ( !drivers.contains(resultDriver.get(j))) {
                                drivers.add(resultDriver.get(j));
                            }
                        }

                    }
                }

            }
        } else {

        }
        if (drivers.size() > 0 && drivers.size() >= sizeVehicle) {
            Collections.sort(drivers, (o1, o2) -> o1.getWorkingHour().compareTo(o2.getWorkingHour()));

            for (int v = 0; v < vehiclesSave.size(); v++) {
                for (int k = 0; k < drivers.size(); k++) {
                    schedule = new Schedule();
                    if (!driversSave.contains(drivers.get(k))) {
                        driversSave.add(drivers.get(k));
                        int license = drivers.get(k).getDriverLicense();
                        if (license == 0 && vehiclesSave.get(v).getWeight() < 3.5) {
                            schedule.setConsignment(consignment);
                            schedule.setImageConsignment("no");
                            schedule.setNote("khong co");
                            schedule.setId(null);
                            schedule.setDriver(drivers.get(k));
                            schedule.setVehicle(vehiclesSave.get(v));
                            schedule.setIsApprove(false);
//                            schedule = scheduleService.createSchedule(schedule);
                            if (schedule != null) {
                                scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
                            }
                            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
                            k = drivers.size();
                        } else if (license > 0 && vehiclesSave.get(v).getWeight() >= 3.5) {


                            schedule.setConsignment(consignment);
                            schedule.setImageConsignment("no");
                            schedule.setNote("khong co");
                            schedule.setId(null);
                            schedule.setDriver(drivers.get(k));
                            schedule.setVehicle(vehiclesSave.get(v));
                            schedule.setIsApprove(false);
//                            schedule = scheduleService.createSchedule(schedule);
                            if (schedule != null) {
                                scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
                            }
                            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
                            k = drivers.size();
                        }

                    }

                }
            }


        } else if (drivers.size() > 0 && drivers.size() <= sizeVehicle) {
            scheduleToConfirmDTO.setDriverForScheduleDTOS(driverForScheduleDTO.mapToListResponse(drivers));
        }
        scheduleToConfirmDTO.setScheduleForConsignmentDTOS(scheduleForConsignmentDTOS);
        if(scheduleToConfirmDTO.getScheduleForConsignmentDTOS().size() == sizeVehicle){
            scheduleToConfirmDTO.setQuantity(0);
        }else{
            scheduleToConfirmDTO.setQuantity(sizeVehicle - scheduleToConfirmDTO.getScheduleForConsignmentDTOS().size() );
        }
//        scheduleToConfirmDTOS.add(scheduleToConfirmDTO);
        return scheduleToConfirmDTO;
    }

    @PostMapping("/status")
    public ResponseEntity<ConsignmentResponseDTO> updateStatusSchedules(@RequestBody RequestSaveScheObjDTO requestSaveScheObjDTO){
        boolean result = false;
        Consignment consignment = new Consignment();
        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        if(requestSaveScheObjDTO!=null){
            List<ObejctScheDTO> obejctScheDTOS =requestSaveScheObjDTO.getObejctScheDTOS();
            ConsignmentRequestDTO consignmentRequestDTO = requestSaveScheObjDTO.getConsignmentRequestDTO();
            try{
                 consignment = consignmentService.save(consignmentRequestDTO);

                if(consignment.getId()!=null){
                    consignmentResponseDTO = consignmentResponseDTO.mapToResponse(consignment);
                    scheduleService.createSchedule(obejctScheDTOS, consignment);
                    result = true;

                }
                if(result){
                    return ResponseEntity.ok().body(consignmentResponseDTO);
                }
//            for(int i =0 ; i<requestObjectDTOS.size(); i++){
//                boolean s =  scheduleService.updateStatusSchedule(requestObjectDTOS.get(i));
//                if(!s){
//                    result.add(requestObjectDTOS.get(i).getConsignment_id());
//                }
//            }

            }catch (Exception e){
                return ResponseEntity.badRequest().build();
            }

        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<List<ScheduleForConsignmentDTO>> getScheduleForVehicle(@PathVariable int id){
        List<ScheduleForConsignmentDTO> result =new ArrayList<>();
        try{
            result = scheduleService.getScheduleForVehicle(id);
            if(result.size()>0){
                return  ResponseEntity.ok().body(result);
            }else{
                return ResponseEntity.noContent().build();
            }

        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
//        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ScheduleResponseDTO>> searchByTypeForDriver(@RequestParam SearchTypeForDriverEnum searchType, @RequestParam String searchValue){
        List<Schedule> result =new ArrayList<>();
        try{
            result = scheduleService.searchByTypeForDriver(searchValue,searchType);
            if(result.size()>0){
                List<ScheduleResponseDTO> consignmentResponses = new ArrayList<>(new ScheduleResponseDTO().mapToListResponse(result));
                return  ResponseEntity.ok().body(consignmentResponses);
            }else{
                return ResponseEntity.noContent().build();
            }

        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
//        return ResponseEntity.ok().body(result);
    }
}
