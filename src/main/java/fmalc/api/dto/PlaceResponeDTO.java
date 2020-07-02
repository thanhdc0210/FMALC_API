package fmalc.api.dto;

import fmalc.api.entity.Place;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
public class PlaceResponeDTO {
    private int id;
    private Double latitude;
    private Double longitude;
    private String address;
    private String name;
    private Timestamp plannedTime;
    private Timestamp actualTime;
    private Integer type;

    public PlaceResponeDTO convertPlace(Place place){
        ModelMapper modelMapper = new ModelMapper();
        PlaceResponeDTO placeResponeDTO= modelMapper.map(place,PlaceResponeDTO.class);
        return placeResponeDTO;
    }
}
