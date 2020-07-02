package fmalc.api.dto;

import fmalc.api.entity.Driver;
import fmalc.api.entity.MaintainType;
import fmalc.api.entity.Maintenance;
import fmalc.api.entity.Vehicle;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MaintainCheckDTO {
    private Integer id;
    private String imageMaintain;
    private Integer kmOld;
    private Date maintainDate;

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
