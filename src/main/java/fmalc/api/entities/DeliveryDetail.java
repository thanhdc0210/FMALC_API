package fmalc.api.entities;

import java.io.Serializable;
import java.util.Collection;

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
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id")
    private Integer id;

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "received_place_id", referencedColumnName = "id", insertable = false)
    private Collection<Received_Place> receivedPlaces;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "consignment_id", referencedColumnName = "id", insertable = false)
    private Consignment consignment;

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "deliveried_place_id", referencedColumnName = "id", insertable = false)
    private Collection<Deliveried_Place> deliveriedPlaces;
}