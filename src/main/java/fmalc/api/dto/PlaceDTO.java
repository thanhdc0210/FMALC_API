package fmalc.api.dto;

import fmalc.api.entity.Place;
import fmalc.api.enums.TypeLocationEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {

    private Double latitude;
    private Double longitude;
    private String address;
    private String name;
    private Timestamp plannedTime;
    private String type; // Giao hàng hay nhận hàng
    private Integer priority; // trình tự giao nhận hàng
    private Timestamp actualTime;

    public PlaceDTO(Place place){
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.address = place.getAddress();
        this.name = place.getName();
        this.plannedTime = place.getPlannedTime();
        this.actualTime = place.getActualTime();
        this.type = TypeLocationEnum.getValueEnumToShow(place.getType());
        this.priority = place.getPriority();
    }
}
