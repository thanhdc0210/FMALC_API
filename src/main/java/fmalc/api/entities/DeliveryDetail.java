package fmalc.api.entities;

import java.io.Serializable;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "delivery_detail")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DeliveryDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "received_place_id", referencedColumnName = "id", nullable = false)
    private ReceivedPlace receivedPlaces;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "consignment_id", referencedColumnName = "id", nullable = false)
    private Consignment consignment;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "delivered_place_id", referencedColumnName = "id", nullable = false)
    private DeliveredPlace deliveredPlaces;
}