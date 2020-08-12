package fmalc.api.service;

import fmalc.api.dto.DayOffRequestDTO;
import fmalc.api.dto.DriverRequestDTO;
import fmalc.api.dto.ScheduleForConsignmentDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Driver;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DriverService {

    List<Driver> findAllAndSearch(String searchPhone);

    List<Driver> findAllAndSearchByFleet(int idFleet,String searchPhone);

    Driver findDiverByPhoneNumberOrIdentityNoOrNo(String phone, String indentityNo, String no);

    Driver findById(Integer id);

    Driver save(DriverRequestDTO driverRequest, MultipartFile file) throws IOException;

    Driver update(Integer id, DriverRequestDTO driverRequest) throws Exception;

    List<Driver> findDriverByLicense(double weight);

    List<Driver> getListDriverByLicense(double weight, int status);

    int updateStatus(int status, int id);

    Driver updateAvatar(Integer id, MultipartFile file) throws IOException;

    List<Driver> findAllByFleetManager(Integer id);

    Driver findDriverByUsername(String username);

    List<Driver> findDriverForSchedule(double weight, Consignment consignment);

    List<ScheduleForConsignmentDTO> checkScheduleForDriver(int idDriver);

    String updateTokenDevice(Driver driver);

    String findTokenDeviceByDriverId(Integer id);

    void createDayOff(DayOffRequestDTO dayOffRequestDTO);
}
