package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery")
public class Delivery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "delivery_time")
    private Timestamp deliveryTime;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "place_of_delivery")
    private String placeOfDelivery;

    
}