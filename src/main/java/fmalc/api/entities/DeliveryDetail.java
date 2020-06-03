package fmalc.api.entities;

import java.io.Serializable;
import javax.persistence.*;

import lombok.*;

@Table(name = "delivery_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DeliveryDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @JoinColumn(name = "id_consignment", nullable = false)
    @OneToOne
    private Consignment idConsignment;

    @JoinColumn(name = "id_delivery", nullable = false)
    @ManyToOne
    private Delivery idDelivery;

    @JoinColumn(name = "id_receipt", nullable = false)
    @ManyToOne
    private Receipt idReceipt;

    
}