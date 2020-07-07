
package fmalc.api.service.impl;

import fmalc.api.dto.VehicleForDetailDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Vehicle;
import fmalc.api.enums.VehicleStatusEnum;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {


    @Autowired
    VehicleRepository vehicleRepository;

    @Override

    public Vehicle saveVehicle(Vehicle vehicle) {
         vehicleRepository.saveAndFlush(vehicle);
//        vehicleRepository.save(vehicle);
        return vehicle;
    }

    @Override
    public VehicleForDetailDTO findVehicleById(int id) {
        Vehicle vehicle =   vehicleRepository.findByIdVehicle(id);
        VehicleForDetailDTO vehicleDTO = new VehicleForDetailDTO();
        vehicleDTO = vehicleDTO.convertToDto(vehicle);
        return vehicleDTO;
    }

    @Override
    public Vehicle findVehicleByIdForLocation(int id) {
        return vehicleRepository.findByIdVehicle(id);
    }




    @Override
    public List<Vehicle> getListVehicle() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles = vehicleRepository.findAll();
        return vehicles;
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
        List<Vehicle> vehicles = vehicleRepository.findByWeight(weight);
        List<Vehicle> result =new ArrayList<>();
        for(int i = 0; i< vehicles.size(); i ++){
            if(vehicles.get(i).getStatus() != VehicleStatusEnum.SOLD.getValue()){
                result.add(vehicles.get(i));
            }
        }
        return result;
    }
//    public List<String> findVehicleLicensePlatesForReportInspection(List<Integer> status, String username, Timestamp currentDate) {
//        return vehicleRepository.findVehicleLicensePlatesForReportInspection(status, username, currentDate);
//
//    }

    @Override
    public Vehicle getVehicleByKmRunning( List<Vehicle> vehicles) {
        return Collections.min(vehicles, Comparator.comparing(s -> s.getKilometerRunning()));
    }

    @Override
    public void updateStatus(int status, int id) {

         vehicleRepository.updateStatusVehicle(status, id);

    }
}

