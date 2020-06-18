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
@Table(name = "place")
public class Place implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "place", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<DeliveryDetail> deliveryDetail;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "planned_time", nullable = false)
    private Timestamp plannedTime;

    @Column(name = "actual_time")
    private Timestamp actualTime;

    @Column(name = "type", nullable = false)
    private Integer type;

    public Place(Timestamp plannedTime, String name, String address) {
        this.address = address;
        this.name = name;
        this.plannedTime = plannedTime;
    }

    public Place(Timestamp plannedTime, String name) {
        this.name = name;
        this.plannedTime = plannedTime;
    }
}