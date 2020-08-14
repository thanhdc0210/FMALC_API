package fmalc.api.dto;

import fmalc.api.entity.Driver;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DriverForDetailDTO {
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
    private FLeetManagerResponseDTO fleetManager;

    public DriverForDetailDTO mapToResponse(Driver baseEntities) {
        ModelMapper modelMapper = new ModelMapper();
        DriverForDetailDTO driverResponse = modelMapper.map(baseEntities, DriverForDetailDTO.class);
        return driverResponse;
    }

    public List<DriverForDetailDTO> mapToListResponse(List<Driver> baseEntities) {
        List<DriverForDetailDTO> result = baseEntities
                .stream()
                .map(driver -> mapToResponse(driver))
                .collect(Collectors.toList());
        return result;
    }
}
