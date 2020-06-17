package fmalc.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivered_place")
public class DeliveredPlace implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "deliveredPlaces", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<DeliveryDetail> deliveryDetail;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "delivered_place_name", nullable = false)
    private String deliveredPlaceName;

    @Column(name = "planned_delivery_time", nullable = false)
    private Timestamp plannedDeliveryTime;

    @Column(name = "actual_delivery_time")
    private Timestamp actualDeliveryTime;

    public DeliveredPlace(Timestamp plannedDeliveryTime, String deliveredPlaceName, String address) {
        this.address = address;
        this.deliveredPlaceName = deliveredPlaceName;
        this.plannedDeliveryTime = plannedDeliveryTime;
    }

    public DeliveredPlace(Timestamp plannedDeliveryTime, String deliveredPlaceName) {
        this.deliveredPlaceName = deliveredPlaceName;
        this.plannedDeliveryTime = plannedDeliveryTime;
    }
}