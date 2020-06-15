package fmalc.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "received_place")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReceivedPlace implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "planned_receive_time", nullable = false)
    private Timestamp plannedReceiveTime;

    @Column(name = "actual_receive_time")
    private Timestamp actualReceiveTime;

    @OneToMany(mappedBy = "receivedPlaces", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<DeliveryDetail> deliveryDetail;

    @Column(name = "received_place_name", nullable = false)
    private String receivedPlaceName;

    public ReceivedPlace(Timestamp plannedReceiveTime, String receivedPlaceName, String address) {
        this.address = address;
        this.plannedReceiveTime = plannedReceiveTime;
        this.receivedPlaceName = receivedPlaceName;
    }

    public ReceivedPlace(Timestamp plannedReceiveTime, String receivedPlaceName) {
        this.plannedReceiveTime = plannedReceiveTime;
        this.receivedPlaceName = receivedPlaceName;
    }
}