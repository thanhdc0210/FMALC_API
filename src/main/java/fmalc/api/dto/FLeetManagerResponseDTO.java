package fmalc.api.dto;

import fmalc.api.entity.*;
import fmalc.api.enums.DriverStatusEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FLeetManagerResponseDTO {
    private Integer id;
    private String identityNo;
    private String name;
    private String phoneNumber;
    private Date dateOfBirth;
    private String image;


    public FLeetManagerResponseDTO mapToResponse(FleetManager fleetManager) {
        ModelMapper modelMapper = new ModelMapper();
        FLeetManagerResponseDTO fLeetManagerResponseDTO = modelMapper.map(fleetManager, FLeetManagerResponseDTO.class);
        return fLeetManagerResponseDTO;
    }

    public List<FLeetManagerResponseDTO> mapToListResponse(List<FleetManager> fleetManagers) {
        return fleetManagers
                .stream()
                .map(fleetManager -> mapToResponse(fleetManager))
                .collect(Collectors.toList());
    }

}
