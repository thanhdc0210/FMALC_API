package fmalc.api.dto;

import fmalc.api.entity.Driver;
import fmalc.api.enums.DriverLicenseEnum;
import fmalc.api.enums.DriverStatusEnum;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DriverResponseDTO {

    private Integer id;
    private Integer status;
    private String driverStatus;
    private Integer driverLicense;
    private String driverLicenseStr;
    private String identityNo;
    private String name;
    private String phoneNumber;
    private Date license_expires;
    private String no;
    private Date dateOfBirth;

    public void setStatus(Integer status) {
        this.status = status;
        this.driverStatus = DriverStatusEnum.getValueEnumToShow(status);
    }


    public void setDriverLicense(Integer driverLicense) {
        this.driverLicense = driverLicense;
        this.driverLicenseStr = DriverLicenseEnum.getValueEnumToShow(driverLicense);
    }

    public DriverResponseDTO mapToResponse(Driver baseEntities) {
        ModelMapper modelMapper = new ModelMapper();
        DriverResponseDTO driverResponse = modelMapper.map(baseEntities, DriverResponseDTO.class);
        driverResponse.setDriverStatus(DriverStatusEnum.getValueEnumToShow(driverResponse.getStatus()));
        return driverResponse;
    }

    public List<DriverResponseDTO> mapToListResponse(List<Driver> baseEntities) {
        List<DriverResponseDTO> result = baseEntities
                .stream()
                .map(driver -> mapToResponse(driver))
                .collect(Collectors.toList());
        return result;
    }
}
