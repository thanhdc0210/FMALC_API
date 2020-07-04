package fmalc.api.dto;

import fmalc.api.entity.Driver;
import fmalc.api.entity.FleetManager;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class FLeetResponseDTO {
    private String name;
    private int id;

    public FLeetResponseDTO convertFleet(FleetManager fleetManager){
        ModelMapper modelMapper = new ModelMapper();
        FLeetResponseDTO fLeetResponseDTO = modelMapper.map(fleetManager, FLeetResponseDTO.class);
        return fLeetResponseDTO;
    }
    public List<FLeetResponseDTO> convertListFleet(List<FleetManager> fleetManagers){

            List<FLeetResponseDTO> result = fleetManagers
                    .stream()
                    .map(fleetManager -> convertFleet(fleetManager))
                    .collect(Collectors.toList());
            return result;

    }
}
