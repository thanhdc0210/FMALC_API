package fmalc.api.dto;

import fmalc.api.entity.Location;
import fmalc.api.entity.Schedule;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LocationResponeDTO implements Serializable {
    private int id;
    private double latitude;
    private  double longitude;
    private String time;
    private String address;
    private int  schedule_id;

    public LocationResponeDTO convertSchedule(Location location){
        ModelMapper modelMapper = new ModelMapper();
        LocationResponeDTO locationResponeDTO = modelMapper.map(location, LocationResponeDTO.class);
        locationResponeDTO.setSchedule_id(location.getSchedule().getId());
        return  locationResponeDTO;
    }
    public List<LocationResponeDTO> mapToListResponse(List<Location> baseEntities) {
        List<LocationResponeDTO> result = baseEntities
                .stream()
                .map(location -> convertSchedule(location))
                .collect(Collectors.toList());
        return result;
    }
}
