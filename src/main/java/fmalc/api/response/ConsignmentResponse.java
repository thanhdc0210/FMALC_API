package fmalc.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentResponse {

    private Integer consignment_id;
    private String owner_name;
    private String received_place_name;
    private String delivered_place_name;
    private Timestamp receive_time;
    private Timestamp delivery_time;
    private String vehicle_plates; // Biển số xe
    private String driver_name;
    private Double weight; // Khối lượng lô hàng

}
