package fmalc.api.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "consignment")
public class Consignment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "consignment", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<Schedule> shedules;

    @OneToMany(mappedBy = "consignment", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<ConsignmentHistory> consignmentHistories;

    @OneToMany(mappedBy = "consignment", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<DeliveryDetail> deliveries;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_note")
    private String ownerNote;

    /**
     * Lí do khách hàng hủy, trì hoãn đơn hàng
     */
    @Column(name = "owner_reason_note")
    private String ownerReasonNote;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "status", nullable = false)
    private Integer status;
}