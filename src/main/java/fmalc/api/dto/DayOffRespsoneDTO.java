package fmalc.api.dto;

import fmalc.api.entity.DayOff;
import fmalc.api.entity.Driver;
import fmalc.api.entity.FleetManager;
import fmalc.api.entity.Fuel;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DayOffRespsoneDTO {
    private Integer id;
    private Date startDate;
    private Date endDate;
    private DriverForDetailDTO driver;
    private String note;
    private Boolean isApprove;

    public DayOffRespsoneDTO convertDTO(DayOff fuel){
        ModelMapper modelMapper = new ModelMapper();
        DayOffRespsoneDTO fuelVehicleDTO = modelMapper.map(fuel, DayOffRespsoneDTO.class);

        return fuelVehicleDTO;
    }


    public List<DayOffRespsoneDTO> mapToListResponse(List<DayOff> fuels) {
        return fuels.stream()
                .map(x -> convertDTO(x))
                .collect(Collectors.toList());
    }
}
