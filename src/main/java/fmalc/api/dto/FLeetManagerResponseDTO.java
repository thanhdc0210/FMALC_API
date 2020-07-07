package fmalc.api.dto;

import fmalc.api.entity.*;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FLeetManagerResponseDTO {
    private Integer id;
    private Integer accountId;
    private String identityNo;
    private String name;
    private String phoneNumber;
    private Date dateOfBirth;
    private String image;
    private Boolean isActive;

    public FLeetManagerResponseDTO mapToResponse(FleetManager fleetManager) {
        ModelMapper modelMapper = new ModelMapper();
        FLeetManagerResponseDTO fLeetManagerResponseDTO = modelMapper.map(fleetManager, FLeetManagerResponseDTO.class);
        fLeetManagerResponseDTO.setIsActive(fleetManager.getAccount().getIsActive());
        fLeetManagerResponseDTO.setAccountId(fleetManager.getAccount().getId());
        return fLeetManagerResponseDTO;
    }

    public List<FLeetManagerResponseDTO> mapToListResponse(List<FleetManager> fleetManagers) {
        return fleetManagers
                .stream()
                .map(fleetManager -> mapToResponse(fleetManager))
                .collect(Collectors.toList());
    }

}
