
package fmalc.api.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.enums.ScheduleConsginmentEnum;
import fmalc.api.enums.SearchTypeForDriverEnum;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    MaintenanceService maintainanceService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PlaceService placeService;

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UploaderService uploaderService;

    private static int priorityPlace = 1;

    @Override
    public ScheduleToConfirmDTO scheduleConfirm(RequestObjectDTO requestObjectDTO, int driver_sub) {
//        private ScheduleToConfirmDTO schedulesConfirm(RequestObjectDTO requestObjectDTO, int driver_sub) throws ParseException {
            ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
            ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
            List<ObejctScheDTO> obejctScheDTOS ;
            Schedule schedule;
            List<Schedule> schedules = new ArrayList<>();
            DriverForScheduleDTO driverForScheduleDTO = new DriverForScheduleDTO();
            obejctScheDTOS = requestObjectDTO.getSchedule();
            ConsignmentRequestDTO consignmentRequestDTO = requestObjectDTO.getConsignmentRequest();
            VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
            List<VehicleConsignmentDTO> vehicleConsignmentDTOS = consignmentRequestDTO.getVehicles();
//        vehicleConsignmentDTOS.sort((VehicleConsignmentDTO v1, VehicleConsignmentDTO v2) -> (v1.getWeight().compareTo(v2.getWeight())));
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

                if (quan > 0) {
                    for (int j = 0; j < obejctScheDTOS.size(); j++) {
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
                    i = vehicleConsignmentDTOS.size();
                }
                obejctScheDTOS.removeAll(removeObject);
            }
            List<Vehicle> vehicles;
            List<Driver> drivers = new ArrayList<>();
            List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
            consignmentRequestDTO.setVehicles(vehicleConsignmentDTOS);
            vehicles = vehicleService.findVehicleForSchedule(consignment, consignmentRequestDTO, ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue());
            for (int ve = 0; ve < vehicles.size(); ve++) {
                List<Driver> driverList;
                driverList = driverService.findDriverForSchedule(vehicles.get(ve).getWeight(), consignment);
                for (int dri = 0; dri < driverList.size(); dri++) {
                    if (!drivers.contains(driverList.get(dri))) {
                        drivers.add(driverList.get(dri));
                    }
                }
            }
            if (total <= schedules.size()) {
                for (int sch = 0; sch < total; sch++) {
                    schedule = new Schedule();
                    if (schedules.get(sch).getInheritance() == null) {
                        schedule.setVehicle(schedules.get(sch).getVehicle());
                        schedule.setDriver(schedules.get(sch).getDriver());
                        schedule.setIsApprove(false);
                        schedule.setConsignment(consignment);
                        schedule.setImageConsignment(consignmentRequestDTO.getImageConsignment());
                        if (schedule != null) {
                            if (driver_sub == 2) {
                                Schedule tmp;
                                tmp = getScheduleByDriverSub(schedules.get(sch).getId());
                                if (tmp != null) {
                                    scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(tmp));
                                } else {
                                    tmp = schedules.get(sch);
                                    Driver driver;
                                    if (drivers.size() > 0) {
                                        for (int i = 0; i < drivers.size(); i++) {
                                            driver = drivers.get(i);
                                            if (driver != schedule.getDriver()) {
                                                tmp.setDriver(driver);
                                            }
                                        }
                                    }
                                    Vehicle vehicle;
                                    if (vehicles.size() > 0) {
                                        for (int i = 0; i < vehicles.size(); i++) {
                                            vehicle = vehicles.get(i);
                                            if (vehicle != schedule.getVehicle()) {
                                                tmp.setVehicle(vehicle);
                                            }
                                        }
                                    }
                                    scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(tmp));
                                }
                            }
                            scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
                        }
                    }
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

                    if (schedule != null) {
                        scheduleForConsignmentDTOS.add(scheduleToConfirmDTO.convertSchedule(schedule));
                    }
                }
//            vehicles = vehicleService.findVehicleForSchedule(consignment, consignmentRequestDTO, ScheduleConsginmentEnum.SCHEDULE_CHECK.getValue());
//            for (int ve = 0; ve < vehicles.size(); ve++) {
//                List<Driver> driverList;
//                driverList = driverService.findDriverForSchedule(vehicles.get(ve).getWeight(), consignment);
//                for (int dri = 0; dri < driverList.size(); dri++) {
//                    if (!drivers.contains(driverList.get(dri))) {
//                        drivers.add(driverList.get(dri));
//                    }
//                }
//            }
                scheduleToConfirmDTO.getScheduleForConsignmentDTOS().addAll(scheduleForConsignmentDTOS);
                scheduleToConfirmDTO.getDriverForScheduleDTOS().addAll(driverForScheduleDTO.mapToListResponse(drivers));
                scheduleToConfirmDTO.getVehicleForDetailDTOS().addAll(vehicleForDetailDTO.mapToListResponse(vehicles));
                if (total > 0) {
                    ScheduleToConfirmDTO resultReturn = scheduleReturn(consignment, consignmentRequestDTO, scheduleToConfirmDTO.getScheduleForConsignmentDTOS(), driver_sub);
                    if (resultReturn.getScheduleForConsignmentDTOS().size() > 0) {
                        scheduleToConfirmDTO.getScheduleForConsignmentDTOS().addAll(resultReturn.getScheduleForConsignmentDTOS());
                        scheduleToConfirmDTO.getDriverForScheduleDTOS().addAll(resultReturn.getDriverForScheduleDTOS());
                        scheduleToConfirmDTO.getVehicleForDetailDTOS().addAll(resultReturn.getVehicleForDetailDTOS());
                    }
                    if (resultReturn.getScheduleForConsignmentDTOS().size() == total) {
                        scheduleToConfirmDTO.setQuantity(0);
                    } else if (resultReturn.getScheduleForConsignmentDTOS().size() < total) {
                        scheduleToConfirmDTO.setQuantity(total - resultReturn.getScheduleForConsignmentDTOS().size());
                    }
                }

            }
            return scheduleToConfirmDTO;
//        }
    }

    @Override
    public ScheduleToConfirmDTO scheduleReturn(Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO, List<ScheduleForConsignmentDTO> scheduleds, int driver_sub) {
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
            List<VehicleConsignmentDTO> vehicleConsignmentDTOS = consignmentRequestDTO.getVehicles();
            for(int  v =0; v<vehicleConsignmentDTOS.size(); v++){
                List<Vehicle> vehiclesTmp = new ArrayList<>();
                int size = Integer.parseInt(vehicleConsignmentDTOS.get(v).getQuantity());
                for (int i = 0; i < vehicles.size(); i++) {
                    if (scheduleds.size() > 0) {
                        for (int s = 0; s < scheduleds.size(); s++) {
                            if (vehicles.get(i).getId() != scheduleds.get(s).getVehicle().getId() && !vehiclesSave.contains(vehicles.get(i))) {
                                if(vehicles.get(i).getWeight()>=  Double.parseDouble(vehicleConsignmentDTOS.get(v).getWeight())){
                                    if(!vehiclesTmp.contains(vehicles.get(i))){
                                        vehiclesTmp.add(vehicles.get(i));
                                        if(size>0){
                                            size--;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (!vehiclesSave.contains(vehicles.get(i))) {
                            if(vehicles.get(i).getWeight()>=  Double.parseDouble(vehicleConsignmentDTOS.get(v).getWeight())){
                                if(!vehiclesTmp.contains(vehicles.get(i))){
                                    vehiclesTmp.add(vehicles.get(i));
                                    if(size>0){
                                        size--;
                                    }
                                }
                            }
//                            vehiclesSave.add(vehicles.get(i));
                        }
                    }
                    if(size == 0){
                        vehiclesSave.addAll(vehiclesTmp);
                        break;
                    }
                }
            }

            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTO.mapToListResponse(vehiclesSave));
        } else if (vehiclesSave.size() > 0 && vehiclesSave.size() < sizeVehicle) {
            vehicleForDetailDTOS = vehicleForDetailDTO.mapToListResponse(vehiclesSave);
            scheduleToConfirmDTO.setVehicleForDetailDTOS(vehicleForDetailDTOS);

            // thieu xe
        }

        List<Driver> resultDriver = new ArrayList<>();
        DriverForScheduleDTO driverForScheduleDTO = new DriverForScheduleDTO();
        if (vehicles.size() > 0 && vehicles.size() >= sizeVehicle) {
            for (int i = 0; i < vehicles.size(); i++) {


                resultDriver = driverService.findDriverForSchedule(vehicles.get(i).getWeight(), consignment);
                for (int j = 0; j < resultDriver.size(); j++) {
                    if (scheduleds.size() > 0) {
                        for (int s = 0; s < scheduleds.size(); s++) {
                            if (resultDriver.get(j).getId() != scheduleds.get(s).getDriver().getId() && !drivers.contains(resultDriver.get(j))) {
                                drivers.add(resultDriver.get(j));
                            }
                        }

                    } else {
                        if (!drivers.contains(resultDriver.get(j))) {
                            drivers.add(resultDriver.get(j));
                        }
                    }

                }


            }
        } else {
        }
        if (drivers.size() > 0 && drivers.size() >= sizeVehicle) {
            Collections.sort(drivers, (o1, o2) -> o1.getWorkingHour().compareTo(o2.getWorkingHour()));
            drivers.sort(new Comparator<Driver>() {
                @Override
                public int compare(Driver o1, Driver o2) {

                    if(o1.getSchedules().size()<o2.getSchedules().size()){
                        return -1;
                    }else if(o1.getSchedules().size()>o2.getSchedules().size()){
                        return 1;
                    }else{
                        return 0;
                    }

                }
            });
            for (int v = 0; v < vehiclesSave.size(); v++) {
                for (int k = 0; k < drivers.size(); k++) {
                    schedule = new Schedule();
                    if (!driversSave.contains(drivers.get(k))) {

                        int license = drivers.get(k).getDriverLicense();
                        if (license == 0 && vehiclesSave.get(v).getWeight() < 3.5) {
                            driversSave.add(drivers.get(k));
                            schedule.setConsignment(consignment);
                            schedule.setImageConsignment("no");
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
                        } else if (license > 0) {
                            driversSave.add(drivers.get(k));
                            schedule.setConsignment(consignment);
                            schedule.setImageConsignment("no");
                            schedule.setId(null);
                            schedule.setDriver(drivers.get(k));
                            schedule.setVehicle(vehiclesSave.get(v));
                            schedule.setIsApprove(false);
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
        List<ScheduleForConsignmentDTO> list = new ArrayList<>();
        if (scheduleForConsignmentDTOS.size() > 0 && driver_sub == 2) {
            for (int i = 0; i < scheduleForConsignmentDTOS.size(); i++) {
                ScheduleForConsignmentDTO scheduleForConsignmentDTO = new ScheduleForConsignmentDTO();
                scheduleForConsignmentDTO = scheduleForConsignmentDTOS.get(i);
//            Schedule tmp = new Schedule();
                for (int t = 0; t < drivers.size(); t++) {
                    if (!driversSave.contains(drivers.get(t))) {
                        int license1 = drivers.get(t).getDriverLicense();
                        ScheduleForConsignmentDTO scheduleForConsignment = new ScheduleForConsignmentDTO();
//                        scheduleForConsignment = scheduleForConsignmentDTO;
                        if (license1 > 0) {
                            driversSave.add(drivers.get(t));
                            driverForScheduleDTO = new DriverForScheduleDTO();
                            driverForScheduleDTO = driverForScheduleDTO.convertToDto(drivers.get(t));
                            scheduleForConsignmentDTO.setInheritance(driverForScheduleDTO);
                            t = drivers.size();
                            sizeVehicle--;
//                            list.add(scheduleForConsignment);
                        } else if (license1 == 0 && scheduleForConsignmentDTO.getVehicle().getWeight() < 3.5) {
                            driversSave.add(drivers.get(t));
                            driverForScheduleDTO = new DriverForScheduleDTO();
                            driverForScheduleDTO = driverForScheduleDTO.convertToDto(drivers.get(t));
                            scheduleForConsignmentDTO.setInheritance(driverForScheduleDTO);
                            t = drivers.size();
                            sizeVehicle--;
//                            list.add(scheduleForConsignment);
                        }
                    }
                }
            }
            scheduleForConsignmentDTOS.addAll(list);
        }

        scheduleToConfirmDTO.setScheduleForConsignmentDTOS(scheduleForConsignmentDTOS);
        if (scheduleToConfirmDTO.getScheduleForConsignmentDTOS().size() == sizeVehicle) {
            scheduleToConfirmDTO.setQuantity(0);
        } else {
            scheduleToConfirmDTO.setQuantity(sizeVehicle - scheduleToConfirmDTO.getScheduleForConsignmentDTOS().size());
        }
//        scheduleToConfirmDTOS.add(scheduleToConfirmDTO);
        return scheduleToConfirmDTO;
    }

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
    public List<Schedule> createSchedule(RequestSaveScheObjDTO requestSaveScheObjDTO, MultipartFile file) {
        Consignment consignment = new Consignment();
//        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        List<ObejctScheDTO> obejctScheDTOS = requestSaveScheObjDTO.getObejctScheDTOS();
        List<Schedule> schedules = new ArrayList<>();
        ConsignmentRequestDTO consignmentRequestDTO = requestSaveScheObjDTO.getConsignmentRequestDTO();
        try {
            if(!file.isEmpty()){
                String link = uploaderService.upload(file);
                consignmentRequestDTO.setImageConsignment(link);
            }

            consignment = consignmentService.save(consignmentRequestDTO);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        Schedule schedule = new Schedule();

        if (consignment.getId() != null) {
            for (int i = 0; i < obejctScheDTOS.size(); i++) {
                VehicleForDetailDTO vehicleForDetailDTO = vehicleService.findVehicleById(obejctScheDTOS.get(i).getVehicle_id());
                Vehicle vehicle = vehicleForDetailDTO.convertToEnity(vehicleForDetailDTO);
                Driver driver = driverService.findById(obejctScheDTOS.get(i).getDriver_id());
                schedule = new Schedule();

                schedule.setImageConsignment("");
                schedule.setConsignment(consignment);
                schedule.setDriver(driver);
                schedule.setVehicle(vehicle);
                schedule.setIsApprove(true);
                schedule.setId(null);
                schedule = scheduleRepository.save(schedule);
                if(obejctScheDTOS.get(i).getConsignment_id()>0){
                    driver =driverService.findById(obejctScheDTOS.get(i).getConsignment_id());
                   Schedule tmp = new Schedule();
                    tmp.setImageConsignment("");
                    tmp.setConsignment(consignment);
                    tmp.setDriver(driver);
                    tmp.setVehicle(vehicle);
                    tmp.setIsApprove(true);
                    tmp.setId(null);
                    tmp.setInheritance(schedule);
                    tmp = scheduleRepository.save(tmp);
                    schedules.add(tmp);
                }

                schedules.add(schedule);
            }

        }

        return schedules;
    }

    @Override
    public Schedule getScheduleByVehicleAndConsignment(int idCon, int idVehicle) {
        return scheduleRepository.getScheduleByVehicleAndConsignment(idCon,idVehicle);
    }

    @Override
    public List<ScheduleForConsignmentDTO> getScheduleForVehicle(int idVehicle) {
        List<Schedule> schedules = scheduleRepository.checkVehicleInScheduled(idVehicle, ConsignmentStatusEnum.COMPLETED.getValue());
        ScheduleForConsignmentDTO sc = new ScheduleForConsignmentDTO();
        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = sc.mapToListResponse(schedules);
        return scheduleForConsignmentDTOS;
    }

    @Override
    public ConsignmentResponseDTO createSchedules(MultipartFile file, String request) {
        boolean result = false;
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        JsonArray jsonArray = (JsonArray) jsonObject.get("obejctScheDTOS");
        List<ObejctScheDTO> obejctScheDTOS = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject post_id = (JsonObject) jsonArray.get(i);
            ObejctScheDTO obejctScheDTO = new ObejctScheDTO();
            post_id.get("vehicle_id").getAsInt();
            obejctScheDTO.setVehicle_id(post_id.get("vehicle_id").getAsInt());
            obejctScheDTO.setDriver_id(post_id.get("driver_id").getAsInt());
            if (post_id.get("consignment_id") != null) {
                obejctScheDTO.setConsignment_id(post_id.get("consignment_id").getAsInt());
            }
            obejctScheDTOS.add(obejctScheDTO);
        }
        ConsignmentRequestDTO consignmentRequestDTO = new Gson().fromJson(jsonObject.get("consignmentRequestDTO"), ConsignmentRequestDTO.class);
        RequestSaveScheObjDTO requestSaveScheObjDTO1 = new RequestSaveScheObjDTO();
        requestSaveScheObjDTO1.setConsignmentRequestDTO(consignmentRequestDTO);
        requestSaveScheObjDTO1.setObejctScheDTOS(obejctScheDTOS);
        List<Schedule> schedules = new ArrayList<>();
        ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
        if (request != null) {
            schedules = createSchedule(requestSaveScheObjDTO1, file);
            try {
                if (schedules.size() > 0) {
                    Consignment consignment = consignmentService.findById(schedules.get(0).getConsignment().getId());
                    consignmentResponseDTO = consignmentResponseDTO.mapToResponse(consignment);
                    NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
                    for (Schedule schedule : schedules) {
                        notificationRequestDTO.setVehicle_id(schedule.getVehicle().getId());
                        notificationRequestDTO.setDriver_id(schedule.getDriver().getId());
                        notificationRequestDTO.setStatus(false);
                        notificationRequestDTO.setContent("Bạn có lịch chạy mới của lô hàng #" + schedule.getConsignment().getId());
                        notificationRequestDTO.setType(NotificationTypeEnum.TASK_SCHEDULE.getValue());
                        notificationService.createNotification(notificationRequestDTO);
                    }

                    return (consignmentResponseDTO);
                }

            } catch (Exception e) {
                return null;
            }

        }
        return consignmentResponseDTO;
    }

    @Override
    public List<ScheduleForLocationDTO> getScheduleToCheck() {
        ScheduleForLocationDTO scheduleForLocationDTO = new ScheduleForLocationDTO();
        List<Schedule> schedules = scheduleRepository.findAll();

        List<ScheduleForLocationDTO> scheduleForLocationDTOS = scheduleForLocationDTO.mapToListResponse(schedules);
        for (int i = 0; i < schedules.size(); i++) {
            scheduleForLocationDTOS.get(i).setVehicle_id(schedules.get(i).getVehicle().getId());
            scheduleForLocationDTOS.get(i).setDriver_id(schedules.get(i).getDriver().getId());
        }
        return scheduleForLocationDTOS;
    }

    @Override
    public List<Schedule> checkVehicleInScheduled(int idVehicle) {
        List<Schedule> schedules = scheduleRepository.checkVehicleInScheduled(idVehicle, ConsignmentStatusEnum.COMPLETED.getValue());
        return schedules;
    }

    // Get the list of schedules of the driver
    @Override
    public List<Schedule> findByConsignmentStatusAndUsername(List<Integer> status, String username) {


        return scheduleRepository.findByConsignmentStatusAndUsername(status, username);
    }

    @Override
    public Schedule findById(Integer id) {
        if (!scheduleRepository.existsById(id)) {
            return null;
        }
        return scheduleRepository.findById(id).get();
    }

    @Override
    public Schedule findScheduleByVehDriCon(ObejctScheDTO obejctScheDTO) {
        return scheduleRepository.
                findScheduleByVeDriCons(obejctScheDTO.getDriver_id(), obejctScheDTO.getVehicle_id(), obejctScheDTO.getConsignment_id());
    }

    @Override
    public List<Schedule> checkDriverInScheduled(int idDriver) {
        List<Schedule> schedules = scheduleRepository.checkDriverInScheduled(idDriver);
        return schedules;
    }

    @Override
    public int checkConsignmentStatus(int idDriver, int status, int statusDe) {
        return scheduleRepository.checkConsignmentStatus(idDriver, status, statusDe).size();
    }

    @Override
    public Schedule getScheduleRunningForDriver(int idDriver) {
        return scheduleRepository.findConsignmentRuning(idDriver, ConsignmentStatusEnum.OBTAINING.getValue(),ConsignmentStatusEnum.DELIVERING.getValue());
    }

    @Override
    public StatusToUpdateDTO updateStautsForVeDriAndCon(StatusToUpdateDTO statusToUpdateDTO, Schedule schedule) {
        StatusToUpdateDTO status = new StatusToUpdateDTO();

        status.setVehicle_status(vehicleService.updateStatus(statusToUpdateDTO.getVehicle_status(), schedule.getVehicle().getId()));
        status.setDriver_status(driverService.updateStatus(statusToUpdateDTO.getDriver_status(), schedule.getDriver().getId()));
//
        schedule.setStatus(statusToUpdateDTO.getConsignment_status());
        Consignment consignment = consignmentService.findById(schedule.getConsignment().getId());
        if(consignment.getStatus()==0){
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
            consignmentService.updateStatus(statusToUpdateDTO.getConsignment_status(), schedule.getConsignment().getId());
        }else{
           if(statusToUpdateDTO.getConsignment_status()>0){
               schedule.setStatus(statusToUpdateDTO.getConsignment_status());
               scheduleRepository.save(schedule);
           }
        }


        List<Schedule> schedules = scheduleRepository.getScheduleByConsignmentId(schedule.getConsignment().getId());
        for( int i =0; i< schedules.size();i++){
            if(schedules.get(i).getStatus() == statusToUpdateDTO.getConsignment_status()){
                if(i==schedules.size()-1){
                    if(statusToUpdateDTO.getConsignment_status()>=0){
                        consignmentService.updateStatus(statusToUpdateDTO.getConsignment_status(), schedule.getConsignment().getId());
                    }
                }
            }else{
                i = schedules.size();
            }

        }
        status.setConsignment_status(consignment.getStatus());

        return status;
    }

    @Override
    public List<Schedule> searchByTypeForDriver(String value, SearchTypeForDriverEnum searchType, Integer driverId) {
        List<Schedule> result = new ArrayList<>();
        switch (searchType) {
            case CONSIGNMENT_ID:
                Integer parsed = Integer.parseInt(value);
                result = scheduleRepository.findScheduleByConsignmentIdAndDriverIdAndIsApprove(parsed, driverId,true);
                return result;
            case LICENSE_PLATE:
                result = scheduleRepository.findByVehicleLicensePlatesContainingAndDriverIdAndIsApprove(value, driverId,true);
                return result;
            case OWNER_NAME:
                result = scheduleRepository.findByConsignmentOwnerNameContainingAndDriverIdAndIsApprove(value, driverId,true);
                return result;
            default:
                throw new IllegalStateException("Unexpected value: " + searchType);
        }

    }

    @Override
    public Schedule getScheduleByDriverSub(int id) {
        return scheduleRepository.findScheduleBySchedule(id);
    }

    // Start date : lúc bấm nút kết thúc   --  THANHDC
    @Override
    public Integer countScheduleNumberInADayOfDriver(Integer id, Timestamp startDate, Timestamp endDate) {

        return scheduleRepository.countScheduleNumberInADayOfDriver(id, startDate, endDate);
    }

    // THANHDC

    public Integer findScheduleIdByConsignmentIdAndDriverId(Integer consignmentId, Integer driverId){
        return scheduleRepository.findScheduleIdByConsignmentIdAndDriverId(consignmentId, driverId);
    }

    @Override
    public Integer findScheduleIdByContentOfNotificationAndDriverId(String content, Integer driver_id) {

        String subString[] = content.split("#");
        String subStringId[] = subString[subString.length - 1].split("\\s");
        Integer consignmentId =  Integer.valueOf(subStringId[0]);

        return scheduleService.findScheduleIdByConsignmentIdAndDriverId(consignmentId, driver_id);
    }

    @Override
    public Integer findConsignmentFirst(int idDriver) {
        List<Consignment> consignments = scheduleRepository.findConsignmentFirst(idDriver);
//        consignments.sort(Comparator.comparing(Cons));
        if(consignments.size()>0){
            return consignments.get(0).getId();
        }
        return 0;
    }

//    @Override
//    public Schedule findScheduleByConsignment_IdAndDriver_Id(Integer consignmentId, Integer driverId) {
//
//        return scheduleRepository.findScheduleByConsignment_IdAndDriver_Id(consignmentId, driverId);
//    }

}

