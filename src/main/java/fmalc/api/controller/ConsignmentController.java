package fmalc.api.controller;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.ScheduleConsginmentEnum;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            List<Consignment> consignments = consignmentService.getConsignmentOfDriver(id, ConsignmentStatusEnum.WAITING.getValue());
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
            int i = consignmentService.updateConsignment(consignmentUpdateDTO, username);
            if(i>0){
                return ResponseEntity.ok().body(i);
            }else{
                return ResponseEntity.noContent().build();
            }
        }
        catch (Exception e){
            return  ResponseEntity.badRequest().build();
        }

    }



    @GetMapping(value = "status")
    public ResponseEntity<Paging> getAllByStatus(@RequestParam("status") Integer status, @RequestParam("username") String username
    ,@RequestParam("page") Integer page,@RequestParam("type") Integer type,@RequestParam("search") String search) {
        try{
            Paging paging = consignmentService.getAllByStatus(status,username,type,page,search);
            if (paging.getList()!=null && paging.getList().isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(paging);
        }catch (Exception e){
            return  ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("cancel/{id}/{username}")
    public ResponseEntity<Integer> cancelConsignment(@PathVariable("id") int id,@PathVariable("username") String username, @RequestBody String content){
        try{
            content = content.replaceAll("\"","");
            Consignment consignment = consignmentService.cancelConsignment(id,content,username);
            if(consignment.getStatus() == ConsignmentStatusEnum.CANCELED.getValue()){
                return ResponseEntity.ok().body(consignment.getStatus());
            }else{
                return  ResponseEntity.noContent().build();
            }

        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping
    public ResponseEntity<NewScheduleDTO> createConsignment(@RequestBody ConsignmentRequestDTO consignmentRequestDTO) {
        try {
            NewScheduleDTO newScheduleDTO = consignmentService.findVehicleDriver(consignmentRequestDTO);
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

    @PostMapping("places/id")
    public ResponseEntity<PlaceResponeDTO> updateActualTime(@RequestParam("id") Integer id, @RequestParam("schedule") int idSchedule)  {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        try{
            placeResponeDTO = placeService.updateActualTime(id,idSchedule);
        }catch (Exception e){
            ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(placeResponeDTO);
    }

    @GetMapping("find-for-alert")
    public Integer findConsignmentIdForAlert(@RequestParam("driverId") Integer driverId,
                                     @RequestParam("vehicleId") Integer vehicleId) {
        return consignmentService.findConsignmentId(driverId, vehicleId);
    }

}
