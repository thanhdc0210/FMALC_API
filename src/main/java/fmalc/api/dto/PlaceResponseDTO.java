package fmalc.api.dto;

import fmalc.api.enums.TypeLocationEnum;
import lombok.Data;

import java.util.Date;

@Data
public class PlaceResponseDTO {
    private Integer id;
    private Double latitude;
    private Double longitude;
    private String address;
    private String name;
    private Date plannedTime;
    private Date actualTime;
    private Integer type;
    private String typeStr;

    public void setType(Integer type) {
        this.type = type;
        this.typeStr = TypeLocationEnum.getValueEnumToShow(type);
    }
}
