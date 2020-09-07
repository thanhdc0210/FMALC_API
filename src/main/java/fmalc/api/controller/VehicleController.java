package fmalc.api.controller;


import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.service.FuelHistoryService;
import fmalc.api.service.InspectionService;
import fmalc.api.service.ScheduleService;
import fmalc.api.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/vehicles")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @Autowired
    InspectionService inspectionService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    FuelHistoryService fuelHistoryService;

    private static int defaultKilometRunning = 0;

    @GetMapping("/listVehicles")
    public ResponseEntity<Paging> getListVehicle(@RequestParam("page") int page,@RequestParam("status") int status,@RequestParam("license") String license) {
            Paging vehicles = vehicleService.getListVehicle(page, license,status);

        if (vehicles.getList().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

//        List<VehicleResponseDTO> vehicleDTOS = vehicles.stream().map(this::convertToDto).collect(Collectors.toList());

        return ResponseEntity.ok().body(vehicles);
    }

    @GetMapping("/vehicles-report")
    public ResponseEntity<List<VehicleResponseDTO>> getListVehicleForReport() {
        List<VehicleResponseDTO> vehicles = vehicleService.findAll();

        if (vehicles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

//        List<VehicleResponseDTO> vehicleDTOS = vehicles.stream().map(this::convertToDto).collect(Collectors.toList());

        return ResponseEntity.ok().body(vehicles);
    }

    private VehicleResponseDTO convertToDto(Vehicle vehicleType) {
        ModelMapper modelMapper = new ModelMapper();
        VehicleResponseDTO dto = modelMapper.map(vehicleType, VehicleResponseDTO.class);

        return dto;
    }

    @GetMapping("/detailVehicle/{id}")
    public ResponseEntity<VehicleForDetailDTO> getDetailVehicleById(@PathVariable int id) {
        VehicleForDetailDTO vehicle = vehicleService.findVehicleById(id);
        if (vehicle == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(vehicle);
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<Boolean> disableVehicle(@PathVariable("id") int id) {

        try {
            Vehicle vehicle = vehicleService.disableVehicle(id);
            if(vehicle!=null){
                if (vehicle.getIsActive() == false) {
                    return ResponseEntity.ok().body(true);
                }
            }else{
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("schedule-check/{id}")
    public ResponseEntity<Integer> checkSchduleOfVehicle(@PathVariable("id") int id){
        try{
            int result =0;
            List<Schedule> schedules = scheduleService.checkVehicleInScheduled(id);
            if(schedules.size()>0){
                for(int i=0; i< schedules.size();i++){
                    Consignment consignment = schedules.get(i).getConsignment();
                    if(consignment.getStatus()!= ConsignmentStatusEnum.COMPLETED.getValue()
                    && consignment.getStatus()!= ConsignmentStatusEnum.CANCELED.getValue()
                    ){
                        result++;
                    }
                }
            }else{

            }

            return  ResponseEntity.ok().body(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/detail/{licensePlates}")
    public ResponseEntity<VehicleForDetailDTO> getDetailVehicleByLicensePlates(@PathVariable String licensePlates) {
        Vehicle vehicle = vehicleService.findVehicleByLicensePlates(licensePlates);
        VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
        vehicleForDetailDTO = vehicleForDetailDTO.convertToDto(vehicle);
        if (vehicle == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(vehicleForDetailDTO);
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody VehicleForNewDTO dto) throws ParseException {
        Vehicle vehicle = new Vehicle();

        String dateString = dto.getDateOfManufacture(); //

        java.util.Date sqlDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        vehicle = convertToVehicleEntity(dto);
        vehicle.setStatus(VehicleStatusEnum.AVAILABLE.getValue());
        vehicle.setKilometerRunning(dto.getKilometerRunning());
        vehicle.setDateOfManufacture(sqlDate);
        vehicle.setDriverLicense(dto.getDriverLicense());
        vehicle.setIsActive(true);
        Vehicle checkLicensePlate = vehicleService.findVehicleByLicensePlates(dto.getLicensePlates());

        if (checkLicensePlate == null) {

            vehicle = vehicleService.saveVehicle(vehicle);

            if (vehicle == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(vehicle);
        } else {
            return ResponseEntity.noContent().build();
        }


    }

    @PutMapping("/")
    public ResponseEntity<VehicleForDetailDTO> updateVehicle(@RequestBody VehicleForDetailDTO dto) throws ParseException {
        Vehicle vehicle = new Vehicle();
        VehicleForDetailDTO vehicleForDetailDTO = new VehicleForDetailDTO();
        vehicleForDetailDTO = dto;
        String dateString = dto.getDateOfManufacture(); //

        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        vehicle = vehicleService.findById(dto.getId());
//        vehicle = vehicleForDetailDTO.convertToEnity(dto);
//        vehicle.setKilometerRunning(dto.getKilometerRunning());
        vehicle.setVehicleName(dto.getVehicleName());
        vehicle.setAverageFuel(dto.getAverageFuel());
        vehicle.setMaximumCapacity(dto.getMaximumCapacity());
        vehicle.setWeight(dto.getWeight());
        vehicle.setDateOfManufacture(sqlDate);
        vehicle.setDriverLicense(dto.getDriverLicense());
        if(vehicle.getStatus()==3 && dto.getStatus()==0){
            vehicle.setStatus(dto.getStatus());
            vehicle.setIsActive(true);
        }
//        vehicle.setKilometerRunning(dto.getKilometerRunning());
//        Vehicle checkLicensePlate = vehicleService.findVehicleByLicensePlates(dto.getLicensePlates());
        vehicle = vehicleService.updateVehicle(vehicle);
        vehicleForDetailDTO = vehicleForDetailDTO.convertToDto(vehicle);
        if (vehicle == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(vehicleForDetailDTO);


    }

    private Vehicle convertToVehicleEntity(VehicleForNewDTO vehicleForNewDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Vehicle vehicle = modelMapper.map(vehicleForNewDTO, Vehicle.class);
        return vehicle;
    }


    @GetMapping(value = "/report-inspection-before-delivery")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<InspectionResponseDTO> findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDelivery
            (@RequestParam(value = "username") String username, @RequestParam(value = "status") List<Integer> status) {

        try{
            String vehiclePlate = vehicleService.findLicensePlatesForMakingReportBeforeRunningOrWhileRunning(status, username);

            List<Inspection> inspections = inspectionService.findAll();
            if (inspections == null) {
                return ResponseEntity.noContent().build();
            } else {
                if (vehiclePlate == null) {
                    InspectionResponseDTO inspectionResponseDTO = new InspectionResponseDTO();
                    inspectionResponseDTO.setVehicleLicensePlates("");
                    inspectionResponseDTO.setInspections(inspectionService.findAll());

                    return ResponseEntity.ok().body(inspectionResponseDTO);
                } else {

                    InspectionResponseDTO inspectionResponseDTO = new InspectionResponseDTO();
                    inspectionResponseDTO.setVehicleLicensePlates(vehiclePlate);
                    inspectionResponseDTO.setInspections(inspectionService.findAll());

                    return ResponseEntity.ok().body(inspectionResponseDTO);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
           return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/report-inspection-after-delivery")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<InspectionResponseDTO> findVehicleLicensePlatesAndInspectionForReportInspectionAfterDelivery
            (@RequestParam(value = "username") String username, @RequestParam(value = "status") List<Integer> status) {

       try {
           String vehiclePlate = vehicleService.findLicensePlatesForMakingReportAfterRunning(status, username);
           List<Inspection> inspections = inspectionService.findAll();
           if (inspections == null) {
               return ResponseEntity.noContent().build();
           } else {
               if (vehiclePlate == null) {
                   InspectionResponseDTO inspectionResponseDTO = new InspectionResponseDTO();
                   inspectionResponseDTO.setVehicleLicensePlates("");
                   inspectionResponseDTO.setInspections(inspectionService.findAll());

                   return ResponseEntity.ok().body(inspectionResponseDTO);
               } else {

                   InspectionResponseDTO inspectionResponseDTO = new InspectionResponseDTO();
                   inspectionResponseDTO.setVehicleLicensePlates(vehiclePlate);
                   inspectionResponseDTO.setInspections(inspectionService.findAll());

                   return ResponseEntity.ok().body(inspectionResponseDTO);
               }
           }
       }catch (Exception e){
           return ResponseEntity.badRequest().build();
       }
    }

    @GetMapping(value = "/running")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<String> getVehicleRunning(@RequestParam("username") String username) {
        try {
            List<Integer> status = new ArrayList<>();
            status.add(ConsignmentStatusEnum.OBTAINING.getValue());
            status.add(ConsignmentStatusEnum.DELIVERING.getValue());
            String vehiclePlate = vehicleService.findLicensePlatesForMakingReportBeforeRunningOrWhileRunning(status, username);

            if(vehiclePlate!= null){
                return ResponseEntity.ok().body(vehicleService.findVehicleByLicensePlates(vehiclePlate).getId().toString());
            }
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "check-license-plates")
    boolean checkVehiclePlates(@RequestParam("licensePlates") String licensePlates) {
        return vehicleService.checkLicensePlates(licensePlates);
    }

    @GetMapping("fuel-vehicle/{id}")
    public ResponseEntity<List<FuelVehicleDTO>> getFuelOfVehicle(@PathVariable("id") int id){
        FuelVehicleDTO fuelVehicleDTO = new FuelVehicleDTO();
        try{
            List<FuelVehicleDTO> fuelVehicleDTOS = fuelVehicleDTO.mapToListResponse(fuelHistoryService.getListFuelByVehicleId(id));
            if(fuelVehicleDTOS.size()>0){
                return ResponseEntity.ok().body(fuelVehicleDTOS);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}

