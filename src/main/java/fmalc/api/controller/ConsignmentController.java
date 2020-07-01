package fmalc.api.controller;

import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.dto.ConsignmentResponseDTO;
import fmalc.api.dto.DetailedConsignmentDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.dto.ConsignmentDTO;
import fmalc.api.entity.Driver;
import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.DriverStatusEnum;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.service.ConsignmentService;
import fmalc.api.service.DriverService;
import fmalc.api.service.ScheduleService;
import fmalc.api.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping(value = "driver")
    public ResponseEntity<List<ConsignmentDTO>> findByConsignmentStatusAndUsernameForDriver(@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username){
        List<Consignment> consignments = consignmentService.findByConsignmentStatusAndUsernameForDriver(status, username);

        if (consignments == null){
            return ResponseEntity.noContent().build();
        }
        List<ConsignmentDTO> consignmentResponses = new ArrayList<>(new ConsignmentDTO().mapToListResponse(consignments));

        return ResponseEntity.ok().body(consignmentResponses);
    }

    @GetMapping(value = "fleetManager")
    public ResponseEntity<List<ConsignmentDTO>> findByConsignmentStatusAndUsernameForFleetManager(@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username){
        List<Consignment> consignments = consignmentService.findByConsignmentStatusAndUsernameForFleetManager(status, username);

        if (consignments == null){
            return ResponseEntity.noContent().build();
        }
        List<ConsignmentDTO> consignmentResponses = new ArrayList<>(new ConsignmentDTO().mapToListResponse(consignments));
        System.out.println(consignmentResponses.size());
        return ResponseEntity.ok().body(consignmentResponses);
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<DetailedConsignmentDTO> findById(@PathVariable("id") Integer id){
        Consignment consignment = consignmentService.findById(id);
        if (consignment == null || consignment.equals("")){
            return ResponseEntity.noContent().build();
        }
        DetailedConsignmentDTO detailedConsignmentDTO = new DetailedConsignmentDTO(consignment);

        return ResponseEntity.ok().body(detailedConsignmentDTO);
    }

    @GetMapping
    public ResponseEntity<List<ConsignmentResponseDTO>> getAll() {
        List<Consignment> consignments = consignmentService.findAll();
        if (consignments.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(new ConsignmentResponseDTO().mapToListResponse(consignments));
    }

    @GetMapping(value = "status")
    public ResponseEntity<List<ConsignmentResponseDTO>> getAllByStatus(@RequestParam("status") Integer status) {
        List<Consignment> consignments = consignmentService.getAllByStatus(status);
        if (consignments.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(new ConsignmentResponseDTO().mapToListResponse(consignments));
    }

    @PostMapping
    public ResponseEntity<ConsignmentResponseDTO> createConsignment(@RequestBody ConsignmentRequestDTO consignmentRequestDTO){
        try {
            Consignment consignment = new Consignment();
//            scheduleService= new Sc
            Vehicle vehicle = findVehicleForSchedule();
            if( findDriverForSchedule(vehicle) !=null){
                Driver driver = findDriverForSchedule(vehicle);
                consignment = consignmentService.save(consignmentRequestDTO);
                Schedule schedule = new Schedule();
                schedule.setConsignment(consignment);
                schedule.setDriver(findDriverForSchedule(vehicle));
                schedule.setVehicle(vehicle);
                schedule.setImageConsignment("no");
                schedule.setNote("khong co");
                schedule.setId(null);
                schedule = scheduleService.createSchedule(schedule);
                if(schedule !=null){
//                    vehicle.setStatus(VehicleStatusEnum.SCHEDULED.getValue());
                    vehicleService.updateStatus(VehicleStatusEnum.SCHEDULED.getValue(), vehicle.getId());
                    driverService.updateStatus(DriverStatusEnum.SCHEDULED.getValue(), driver.getId());

//                    driver.setStatus(DriverStatusEnum.SCHEDULED.getValue());

                    System.out.println("Thanh cong "+schedule.getId());
                }else{
                    System.out.println("schedule failed");
                }

//
            }else{
                System.out.println("Null mej roi");
            }
            return ResponseEntity.ok().body(new ConsignmentResponseDTO().mapToResponse(consignment));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    private Vehicle findVehicleForSchedule(){
        List<Vehicle> vehicles = vehicleService.findByStatus(VehicleStatusEnum.AVAILABLE.getValue());
        Vehicle vehicle= new Vehicle();
        if(vehicles.size()>0) {
           vehicle = vehicleService.getVehicleByKmRunning(vehicles);
        }
        return  vehicle;
    }
    private Driver findDriverForSchedule(Vehicle vehicle){

        Driver driver = new Driver();

            if(vehicle !=null){
                double weight = vehicle.getWeight();
                List<Driver> drivers =  driverService.getListDriverByLicense(weight);
                float workingHours = drivers.get(0).getWorkingHour();
                driver = drivers.get(0);
                for (int i =1; i< drivers.size(); i++){
                    float tmp = drivers.get(i).getWorkingHour();
                    if(workingHours > tmp){
                        driver = drivers.get(i);
                    }
                }
            }



        return  driver;
//        List<Consignment> consignments = consignmentService.getAllByStatus(0);
    }
}
