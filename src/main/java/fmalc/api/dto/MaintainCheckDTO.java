package fmalc.api.dto;

import fmalc.api.entity.Maintenance;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MaintainCheckDTO {
    private Integer id;
    private String imageMaintain;
    private Integer kmOld;
    private Date plannedMaintainDate;
    private Date actualMaintainDate;

    public MaintainCheckDTO convertMaintain(Maintenance maintenance){
        ModelMapper modelMapper = new ModelMapper();
        MaintainCheckDTO maintainCheckDTO = modelMapper.map(maintenance, MaintainCheckDTO.class);
        return  maintainCheckDTO;
    }
    public List<MaintainCheckDTO> mapToListResponse(List<Maintenance> baseEntities) {
        List<MaintainCheckDTO> result = baseEntities
                .stream()
                .map(driver -> convertMaintain(driver))
                .collect(Collectors.toList());
        return result;
    }
}
