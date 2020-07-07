package fmalc.api.dto;

import fmalc.api.entity.Driver;
import fmalc.api.entity.Vehicle;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DriverForScheduleDTO {
    private Integer id;
    private Integer status;
    private Integer driverLicense;
    private String identityNo;
    private String name;
    private String phoneNumber;
    private Date license_expires;
    private String no;
    private Date dateOfBirth;
    private Float workingHour;
    private String image;


    public DriverForScheduleDTO convertToDto(Driver driver) {
        ModelMapper modelMapper = new ModelMapper();
        DriverForScheduleDTO dto = modelMapper.map(driver, DriverForScheduleDTO.class);

        return dto;
    }

    public List<DriverForScheduleDTO> mapToListResponse(List<Driver> baseEntities) {
        List<DriverForScheduleDTO> result = baseEntities
                .stream()
                .map(driver -> convertToDto(driver))
                .collect(Collectors.toList());
        return result;
    }

}
