package fmalc.api.response;
import fmalc.api.entities.Driver;
import fmalc.api.enums.DriverStatusEnum;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DriverResponse {

    private Integer id;
    private Integer status;
    private String driverStatus;
    private DriverLicenseResponse driverLicenseResponse;
    private String identityNo;
    private String name;
    private String phoneNumber;

    public DriverResponse mapToResponse(Driver baseEntitie) {
        ModelMapper modelMapper = new ModelMapper();
        DriverResponse driverResponse = modelMapper.map(baseEntitie, DriverResponse.class);
        driverResponse.setDriverStatus(DriverStatusEnum.getValueEnumToShow(driverResponse.getStatus()));
        driverResponse.setDriverLicenseResponse(modelMapper.map(baseEntitie.getLicense(), DriverLicenseResponse.class));
        return driverResponse;
    }

    public List<DriverResponse> mapToListResponse(List<Driver> baseEntities) {
        ModelMapper modelMapper = new ModelMapper();
        List<DriverResponse> result = baseEntities
                .stream()
                .map(driver -> modelMapper.map(driver, DriverResponse.class))
                .collect(Collectors.toList());
        for (int i = 0; i < baseEntities.size(); i++) {
            DriverResponse dto = result.get(i);
            dto.setDriverStatus(DriverStatusEnum.getValueEnumToShow(dto.getStatus()));
            dto.setDriverLicenseResponse(modelMapper.map(baseEntities.get(i).getLicense(), DriverLicenseResponse.class));
        }
        return result;
    }
}
