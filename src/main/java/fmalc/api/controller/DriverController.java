package fmalc.api.controller;

import fmalc.api.entities.Driver;
import fmalc.api.model.DriverDTO;
import fmalc.api.model.DriverLicenseDTO;
import fmalc.api.service.DriverService;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1.0/drivers")
public class DriverController {
    @Autowired
    DriverService driverService;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(path = "/get-all-drivers", method = RequestMethod.GET)
    public List<DriverDTO> getAllDriver() {
        List<Driver> drivers = driverService.findAll();
        List<DriverDTO> result = drivers
                .stream()
                .map(driver -> modelMapper.map(driver, DriverDTO.class))
                .collect(Collectors.toList());
        for (int i = 0; i < drivers.size(); i++) {
            DriverDTO dto = result.get(i);
            dto.setLicenseDTO(modelMapper.map(drivers.get(i).getLicense(), DriverLicenseDTO.class));
        }
        return result;
    }

    @RequestMapping(path = "find-by-id", method = RequestMethod.GET)
    public ResponseEntity findById(@RequestParam("id") Integer id) {
        Driver driver = driverService.findById(id);
        JSONObject jsonObject = new JSONObject();
        if (driver == null) {
            jsonObject.put("message", "Không tìm thấy tài xế");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
        }
        DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);
        return new ResponseEntity(driverDTO, HttpStatus.OK);
    }

    @RequestMapping(path = "create-driver", method = RequestMethod.POST)
    public ResponseEntity createDriver(@RequestBody DriverDTO driverDTO) {
        JSONObject jsonObject = new JSONObject();
        try {
            driverService.save(driverDTO);
            jsonObject.put("message", "Lưu thành công");
            return ResponseEntity.status(HttpStatus.OK).body(jsonObject.toString());
        } catch (Exception ex) {
            jsonObject.put("error", "Lưu thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
        }
    }

    @RequestMapping(path = "change-status", method = RequestMethod.PUT)
    public ResponseEntity changeStatus(@RequestParam("id") Integer id, @RequestParam("status") Integer status) {
        Driver driver = driverService.findById(id);
        JSONObject jsonObject = new JSONObject();
        if (driver == null) {
            jsonObject.put("message", "Không tìm thấy tài xế");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
        } else {
            driverService.changeStatus(driver, status);
            jsonObject.put("message", "Thay đổi thành công");
            return ResponseEntity.status(HttpStatus.OK).body(jsonObject.toString());
        }
    }
}
