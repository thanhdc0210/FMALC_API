package fmalc.api.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consignment")
public class Consignment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

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

}