//package fmalc.api.controller;
//
//
//import fmalc.api.entity.DriverLicense;
//import fmalc.api.entity.VehicleType;
//import fmalc.api.service.DriverLicenseService;
//import fmalc.api.service.VehicleTypeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/vehicleType")
//public class VehicleTypeController {
//
//    @Autowired
//    VehicleTypeService vehicleTypeService;
//
//    @Autowired
//    DriverLicenseService driverLicenseService;
//
////    @PostMapping("/createVehicleType")
////    public ResponseEntity<VehicleType> createVehicleType(VehicleType vehicleType){
////        List<VehicleType> vehicleTypeList = new ArrayList<>();
////        DriverLicense driverLicense = new DriverLicense();
////        DriverLicense driverLicense1 = new DriverLicense();
////        DriverLicense driverLicense2 = new DriverLicense();
////        VehicleType vehicleType1 = new VehicleType();
////        vehicleType1.setAverageFuel(1.5);
////        vehicleType1.setMaximumCapacity(8.0);
//////        vehicleType1.setWeight();
////        vehicleType1.setVehicleTypeName("Xe tải dưới 3 tấn");
////
////        driverLicense=  driverLicenseService.getLicenseByLicenseType("B1");
////        vehicleType1.setDriver_license(driverLicense);
////
////        VehicleType vehicleType2 = new VehicleType();
////        vehicleType2.setAverageFuel(2.0);
////        vehicleType2.setMaximumCapacity(10.0);
////        vehicleType2.setVehicleTypeName("Xe tải  3 - 5 tấn");
////        driverLicense1=  driverLicenseService.getLicenseByLicenseType("C");
////        vehicleType2.setDriver_license(driverLicense1);
////
////        VehicleType vehicleType3 = new VehicleType();
////        vehicleType3.setAverageFuel(2.1);
////        vehicleType3.setMaximumCapacity(15.0);
////        vehicleType3.setVehicleTypeName("Xe tải  5 - 8 tấn");
////        driverLicense2=  driverLicenseService.getLicenseByLicenseType("D");
////        vehicleType3.setDriver_license(driverLicense2);
////
////        vehicleTypeList.add(vehicleType1);
////        vehicleTypeList.add(vehicleType3);
////        vehicleTypeList.add(vehicleType2);
////
////        for(int i =0; i< vehicleTypeList.size(); i++){
////            vehicleTypeService.saveTypeVehicle(vehicleTypeList.get(i));
////        }
////
////        return ResponseEntity.ok().build();
////
////    }
//
//    @GetMapping("/typeOfVehicle")
//    public ResponseEntity<List<VehicleTypeDTO>> getListVehicleType(){
//
//
//        List<VehicleTypeDTO> vehicleTypes = vehicleTypeService.getListVehicleType();
//
//        if(vehicleTypes.isEmpty()){
//            VehicleType vehicleType = new VehicleType();
////            createVehicleType(vehicleType);
//            vehicleTypes = vehicleTypeService.getListVehicleType();
//            return ResponseEntity.ok().body(vehicleTypes);
//        }
//        return ResponseEntity.ok().body(vehicleTypes);
//    }
//}
