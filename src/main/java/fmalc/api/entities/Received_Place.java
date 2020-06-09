package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "received_place")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Received_Place implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "receive_time", nullable = false)
    private Timestamp receiveTime;

    @OneToMany(mappedBy = "receivedPlaces", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<DeliveryDetail> deliveryDetail;

    @Column(name = "received_place_name", nullable = false)
    private String received_place_name;
}