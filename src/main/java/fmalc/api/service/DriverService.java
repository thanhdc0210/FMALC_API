package fmalc.api.service;

import fmalc.api.dto.DriverRequestDTO;
import fmalc.api.entity.Driver;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DriverService {

    List<Driver> findAllAndSearch(String searchPhone);

    Driver findById(Integer id);

    Driver save(DriverRequestDTO driverRequest, MultipartFile file) throws IOException;

    Driver update(Integer id, DriverRequestDTO driverRequest) throws Exception;
    List<Driver> findDriverByLicense(double weight);

    List<Driver> getListDriverByLicense(double weight, int status);
    int updateStatus(int status, int id);

    Driver updateAvatar(Integer id, MultipartFile file) throws IOException;

    Integer findIdByUsername(String username);
}
