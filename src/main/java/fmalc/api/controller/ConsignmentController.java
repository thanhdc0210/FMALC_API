package fmalc.api.controller;

import fmalc.api.dto.*;
import fmalc.api.entity.Consignment;
import fmalc.api.service.ConsignmentService;
import fmalc.api.service.DriverService;
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

//    @GetMapping
//    public ResponseEntity<List<ConsignmentListDTO>> getAll() {
//        List<Consignment> consignments = consignmentService.findAll();
//
////        consignmentListDTOS.
//        return  ResponseEntity.ok().body(consignmentListDTOS);
////        return ResponseEntity.ok().body(new ConsignmentResponseDTO().mapToListResponse(consignments));
//    }

//    @GetMapping(value = "status")
//    public ResponseEntity<List<ConsignmentListDTO>> getAllByStatus(@RequestParam("status") Integer status) {
//        List<Consignment> consignments = consignmentService.getAllByStatus(status);
//        ConsignmentListDTO consignmentListDTO = new ConsignmentListDTO();
//        List<ConsignmentListDTO> consignmentListDTOS = consignmentListDTO.mapToListResponse(consignments);
//        if (consignments.isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
//
//
//
//        for (int i=0; i< consignmentListDTOS.size();i++){
//            List<ScheduleForLocationDTO> schedules = new ArrayList<>();
//            schedules = scheduleService.getScheduleByConsignmentId(consignmentListDTOS.get(i).getId());
//            for(int j=0; j<schedules.size();j++){
//                VehicleForDetailDTO vehicleForDetailDTO;
//                List<VehicleForDetailDTO> vehicleForDetailDTOS = new ArrayList<>();
//
//                Driver driver = new Driver();
//                List<Driver> drivers = new ArrayList<>();
//
//                DriverResponseDTO driverResponseDTO = new DriverResponseDTO();
//                List<DriverResponseDTO> driverResponseDTOS = new ArrayList<>();
//
//                vehicleForDetailDTO = vehicleService.findVehicleById(schedules.get(j).getVehicle_id());
//                vehicleForDetailDTOS.add(vehicleForDetailDTO);
//
//                driver = driverService.findById(schedules.get(j).getDriver_id());
//                drivers.add(driver);
//                driverResponseDTOS = driverResponseDTO.mapToListResponse(drivers);
//                consignmentListDTOS.get(i).setDrivers(driverResponseDTOS);
//                consignmentListDTOS.get(i).setVehicles(vehicleForDetailDTOS);
//            }
//
//        }
//        if (consignments.isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok().body(consignmentListDTOS);
//    }

    @PostMapping
    public ResponseEntity<ConsignmentResponseDTO> createConsignment(@RequestBody ConsignmentRequestDTO consignmentRequestDTO){
        try {
            Consignment consignment = consignmentService.save(consignmentRequestDTO);
//            scheduleService= new Sc
//            Vehicle vehicle = findVehicleForSchedule();
//            if( findDriverForSchedule(vehicle) !=null){
//                Driver driver = findDriverForSchedule(vehicle);
//                Schedule schedule = new Schedule();
//                schedule.setConsignment(consignment);
//                schedule.setDriver(findDriverForSchedule(vehicle));
//                schedule.setVehicle(vehicle);
//                schedule.setImageConsignment("no");
//                schedule.setNote("khong co");
//                schedule.setId(null);
//                schedule = scheduleService.createSchedule(schedule);
//                if(schedule !=null){
////                    vehicle.setStatus(VehicleStatusEnum.SCHEDULED.getValue());
//                    vehicleService.updateStatus(VehicleStatusEnum.SCHEDULED.getValue(), vehicle.getId());
//                    driverService.updateStatus(DriverStatusEnum.SCHEDULED.getValue(), driver.getId());
//
////                    driver.setStatus(DriverStatusEnum.SCHEDULED.getValue());
//
//                    System.out.println("Thanh cong "+schedule.getId());
//                }else{
//                    System.out.println("schedule failed");
//                }
//            }else{
//                System.out.println("Null mej roi");
//            }
            return ResponseEntity.ok().body(new ConsignmentResponseDTO().mapToResponse(consignment));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }


//    private Vehicle findVehicleForSchedule(){
//        List<Vehicle> vehicles = vehicleService.findByStatus(VehicleStatusEnum.AVAILABLE.getValue());
//        Vehicle vehicle= new Vehicle();
//        if(vehicles.size()>0) {
//           vehicle = vehicleService.getVehicleByKmRunning(vehicles);
//        }
//        return  vehicle;
//    }
//    private Driver findDriverForSchedule(Vehicle vehicle){
//        Driver driver = new Driver();
//            if(vehicle !=null){
//                double weight = vehicle.getWeight();
//                List<Driver> drivers =  driverService.getListDriverByLicense(weight);
//                driver = Collections.min(drivers, Comparator.comparing(s -> s.getWorkingHour()));
//            }
//        return  driver;
//    }
}
