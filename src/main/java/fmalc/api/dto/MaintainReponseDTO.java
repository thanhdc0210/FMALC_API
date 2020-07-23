package fmalc.api.dto;

import fmalc.api.entity.*;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MaintainReponseDTO {
    private Integer id;
    private VehicleForDetailDTO vehicle;
    private DriverForScheduleDTO driver;
    private MaintainTypeDTO maintainType;
    private String imageMaintain;
    private Integer kmOld;
    private Date plannedMaintainDate;
    private Date actualMaintainDate;

    public MaintainReponseDTO convertSchedule(Maintenance maintenance){
        ModelMapper modelMapper = new ModelMapper();
        MaintainReponseDTO maintainReponseDTO = modelMapper.map(maintenance, MaintainReponseDTO.class);
        return  maintainReponseDTO;
    }
    public List<MaintainReponseDTO> mapToListResponse(List<Maintenance> baseEntities) {
        List<MaintainReponseDTO> result = baseEntities
                .stream()
                .map(driver -> convertSchedule(driver))
                .collect(Collectors.toList());
        return result;
    }
}
