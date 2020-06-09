package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deliveried_place")
public class Deliveried_Place implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "delivery_time")
    private Timestamp deliveryTime;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "deliveriedPlaces", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<DeliveryDetail> deliveryDetail;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "delivered_place_name", nullable = false)
    private String delivered_place_name;
}