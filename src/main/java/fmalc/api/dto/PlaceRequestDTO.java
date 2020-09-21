package fmalc.api.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

@Data
public class PlaceRequestDTO {
    private Double latitude;
    private Double longitude;
    private String address;
    private String name;
    private Timestamp plannedTime;
    private Timestamp actualTime;
    private Integer type;
    private String contactName;
    private String contactPhone;
    private Integer priority;

//    public void setPlannedTime(Timestamp plannedTime) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.getDefault());
//        sdf.setTimeZone(TimeZone.getTimeZone(""));
//        if(plannedTime!=null){
////            String time
//            this.plannedTime = (Timestamp) sdf.parse(plannedTime+"");
//        }
////        this.plannedTime = plannedTime;
//    }


}
