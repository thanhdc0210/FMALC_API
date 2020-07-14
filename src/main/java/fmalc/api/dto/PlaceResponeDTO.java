package fmalc.api.dto;

import fmalc.api.entity.Place;
import fmalc.api.entity.Schedule;
import fmalc.api.enums.TypeLocationEnum;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PlaceResponeDTO {
    private Integer id;
    private Double latitude;
    private Double longitude;
    private String address;
    private String name;
    private Timestamp plannedTime;
    private Timestamp actualTime;
    private Integer type;
    private Integer priority;
    private String contactName;
    private String contactPhone;
    private String typeStr;

    public void setType(Integer type) {
        this.type = type;
        this.typeStr = TypeLocationEnum.getValueEnumToShow(type);
    }

    public PlaceResponeDTO convertPlace(Place place){
        ModelMapper modelMapper = new ModelMapper();
        PlaceResponeDTO placeResponeDTO= modelMapper.map(place,PlaceResponeDTO.class);
        return placeResponeDTO;
    }

    public List<PlaceResponeDTO> mapToListResponse(List<Place> baseEntities) {
        List<PlaceResponeDTO> result = baseEntities
                .stream()
                .map(place -> convertPlace(place))
                .collect(Collectors.toList());
        return result;
    }
}
