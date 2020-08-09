package fmalc.api.controller;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.ScheduleConsginmentEnum;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@RestController
@RequestMapping(value = "/api/v1.0/consignments")
public class ConsignmentController {

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    MaintenanceService maintainanceService;

    @Autowired
    PlaceService placeService;

    @Autowired
    AccountService accountService;

    @Autowired
    ParkingService parkingService;

    @Autowired
    FleetManagerService fleetManagerService;
    @Autowired
    ConsignmentHistoryService consignmentHistoryService;

    @GetMapping("test")
    public ResponseEntity<List<ScheduleForLocationDTO>> test() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Long plannedTime = finishPlace.getPlannedTime().getTime();
        String s = "";
        List<ScheduleForLocationDTO> scheduleForLocationDTOS = scheduleService.getScheduleToCheck();
        return ResponseEntity.ok().body(scheduleForLocationDTOS);
    }

    @GetMapping("vehicle/{id}")
    public ResponseEntity<List<ConsignmentResponseDTO>> getConsignmentByIdVehicle(@PathVariable("id") int id){
        try{
            List<Consignment> consignments = consignmentService.findConsignmentByVehicle(id);
            if(consignments.size()>0){
                ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
                List<ConsignmentResponseDTO> consignmentResponseDTOS = consignmentResponseDTO.mapToListResponse(consignments);
                return ResponseEntity.ok().body(consignmentResponseDTOS);
            }else{
                return ResponseEntity.noContent().build();
            }
        }catch (Exception e){
            return  ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("driver/{id}")
    public ResponseEntity<List<ConsignmentResponseDTO>> getConsignmentOfDriver(@PathVariable("id") int id){
        try{
            List<Consignment> consignments = consignmentService.getConsignmentOfDriver(id);
            if(consignments.size()>0){
                ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
                List<ConsignmentResponseDTO> consignmentResponseDTOS = consignmentResponseDTO.mapToListResponse(consignments);
                return ResponseEntity.ok().body(consignmentResponseDTOS);
            }else{
                return ResponseEntity.noContent().build();
            }
        }catch (Exception e){
            return  ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ConsignmentDetailDTO> getDetailConsignment(@PathVariable("id") int id){
        try{
            Consignment consignment = consignmentService.findById(id);
            if(consignment!=null){
                ConsignmentDetailDTO consignmentDetailDTO = new ConsignmentDetailDTO();
                consignmentDetailDTO = consignmentDetailDTO.convertToDTO(consignment);
                return ResponseEntity.ok().body(consignmentDetailDTO);
            }else{
                return ResponseEntity.noContent().build();
            }

        }
        catch (Exception e){
          return   ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("update/{username}")
    public ResponseEntity<Integer> updateConsignment(@PathVariable("username") String username, @RequestBody ConsignmentUpdateDTO consignmentUpdateDTO    ){
        try{
            Account accout = accountService.getAccount(username);
            FleetManager fleetManager = fleetManagerService.findByAccount(accout.getId());

            int i = consignmentService.updateConsignment(consignmentUpdateDTO);
            String note ="Cập nhật lại lô hàng số "+consignmentUpdateDTO.getId();
            ConsignmentHistory consignmentHistory = consignmentHistoryService.save(consignmentUpdateDTO.getId(),fleetManager,note);
//            if(consignment!=null){
//                ConsignmentDetailDTO consignmentDetailDTO = new ConsignmentDetailDTO();
//                consignmentDetailDTO = consignmentDetailDTO.convertToDTO(consignment);
                return ResponseEntity.ok().body(i);
//            }else{
//                return ResponseEntity.noContent().build();
//            }

        }
        catch (Exception e){
            return   ResponseEntity.badRequest().build();
        }

    }



    @GetMapping(value = "status")
    public ResponseEntity<List<ConsignmentListDTO>> getAllByStatus(@RequestParam("status") Integer status) {
        List<Consignment> consignments = new ArrayList<>();
        if(status == ConsignmentStatusEnum.DELIVERING.getValue() || status == ConsignmentStatusEnum.OBTAINING.getValue()){
            consignments.addAll(consignmentService.getAllByStatus(ConsignmentStatusEnum.DELIVERING.getValue()));
            consignments.addAll(consignmentService.getAllByStatus(ConsignmentStatusEnum.OBTAINING.getValue()));
        }
         consignments = consignmentService.getAllByStatus(status);
        ConsignmentListDTO consignmentListDTO = new ConsignmentListDTO();
        List<ConsignmentListDTO> consignmentListDTOS = consignmentListDTO.mapToListResponse(consignments);
        if (consignments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }


        for (int i = 0; i < consignmentListDTOS.size(); i++) {
            List<ScheduleForLocationDTO> schedules = new ArrayList<>();
            schedules = scheduleService.getScheduleByConsignmentId(consignmentListDTOS.get(i).getId());
            if (schedules.size() > 0) {
                for (int j = 0; j < schedules.size(); j++) {
                    if (schedules.get(j).isApprove()) {
                        VehicleForDetailDTO vehicleForDetailDTO;
                        List<VehicleForDetailDTO> vehicleForDetailDTOS = new ArrayList<>();

                        Driver driver = new Driver();
                        List<Driver> drivers = new ArrayList<>();

                        DriverResponseDTO driverResponseDTO = new DriverResponseDTO();
                        List<DriverResponseDTO> driverResponseDTOS = new ArrayList<>();

                        vehicleForDetailDTO = vehicleService.findVehicleById(schedules.get(j).getVehicle_id());
                        vehicleForDetailDTOS.add(vehicleForDetailDTO);

                        driver = driverService.findById(schedules.get(j).getDriver_id());
                        drivers.add(driver);
                        driverResponseDTOS = driverResponseDTO.mapToListResponse(drivers);
                        consignmentListDTOS.get(i).setDrivers(driverResponseDTOS);
                        consignmentListDTOS.get(i).setVehicles(vehicleForDetailDTOS);
                    }

                }
            }
        }
        if (consignments.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(consignmentListDTOS);
    }

    @PostMapping("cancel/{id}")
    public ResponseEntity<Integer> cancelConsignment(@PathVariable("id") int id, @RequestBody String content){
        try{
            Consignment consignment = consignmentService.cancelConsignment(id,content);
            return ResponseEntity.ok().body(consignment.getStatus());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }


    }
    @PostMapping
    public ResponseEntity<NewScheduleDTO> createConsignment(@RequestBody ConsignmentRequestDTO consignmentRequestDTO) {
        try {
            NewScheduleDTO newScheduleDTO = new NewScheduleDTO();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone(""));
            Consignment consignment = new Consignment();
            ScheduleToConfirmDTO scheduleToConfirmDTO = new ScheduleToConfirmDTO();
            List<ScheduleToConfirmDTO> scheduleToConfirmDTOS = new ArrayList<>();
            ConsignmentResponseDTO consignmentResponseDTO = new ConsignmentResponseDTO();
            consignmentRequestDTO.setImageConsignment("sdsaas");
            consignment = consignmentService.consignmentConfirm(consignmentRequestDTO);

            ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
            List<Vehicle> vehicles =
                    vehicleService.findVehicleForSchedule(consignment, consignmentRequestDTO, ScheduleConsginmentEnum.SCHEDULE_NOT_CHECK.getValue());
            List<ScheduleForConsignmentDTO> scheduleForLocationDTOS =
                    vehicleService.findScheduleForFuture(vehicles, consignment, consignmentRequestDTO);
            consignmentResponseDTO = consignmentResponseDTO.mapToResponse(consignment);
            scheduleForLocationDTO.setConsignment(consignmentResponseDTO);
            scheduleForLocationDTOS.add(scheduleForLocationDTO);
            if (consignmentRequestDTO.getVehicles().size() <= scheduleForLocationDTOS.size()) {

            } else {

            }

            ParkingDTO parkingDTO = parkingService.getParking();
            newScheduleDTO.setParkingDTO(parkingDTO);
            newScheduleDTO.setScheduleForConsignmentDTOS(scheduleForLocationDTOS);
            return ResponseEntity.ok().body(newScheduleDTO);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // id tạm là id của vehicle
    @GetMapping(value = "complete/{id}")
    public ResponseEntity completeConsignment(@PathVariable("id") int id) {
        try {
            maintainanceService.calculateMaintenanceForVehicle(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("places/id/{id}")
    public ResponseEntity<PlaceResponeDTO> updateActualTime(@PathVariable("id") Integer id)  {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        try{
            placeResponeDTO = placeService.updateActualTime(id);
        }catch (Exception e){
            ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(placeResponeDTO);
    }

}
