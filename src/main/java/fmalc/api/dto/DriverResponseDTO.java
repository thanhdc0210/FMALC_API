package fmalc.api.dto;

import fmalc.api.entity.Driver;
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
    private DriverLicenseResponseDTO driverLicenseResponseDTO;
    private String identityNo;
    private String name;
    private String phoneNumber;
    private Date expires;

    public DriverResponseDTO mapToResponse(Driver baseEntitie) {
        ModelMapper modelMapper = new ModelMapper();
        DriverResponseDTO driverResponse = modelMapper.map(baseEntitie, DriverResponseDTO.class);
        driverResponse.setDriverStatus(DriverStatusEnum.getValueEnumToShow(driverResponse.getStatus()));
        driverResponse.setDriverLicenseResponseDTO(modelMapper.map(baseEntitie.getLicense(), DriverLicenseResponseDTO.class));
        return driverResponse;
    }

    public List<DriverResponseDTO> mapToListResponse(List<Driver> baseEntities) {
        ModelMapper modelMapper = new ModelMapper();
        List<DriverResponseDTO> result = baseEntities
                .stream()
                .map(driver -> modelMapper.map(driver, DriverResponseDTO.class))
                .collect(Collectors.toList());
        for (int i = 0; i < baseEntities.size(); i++) {
            DriverResponseDTO dto = result.get(i);
            dto.setDriverStatus(DriverStatusEnum.getValueEnumToShow(dto.getStatus()));
            dto.setDriverLicenseResponseDTO(modelMapper.map(baseEntities.get(i).getLicense(), DriverLicenseResponseDTO.class));
        }
        return result;
    }
}
