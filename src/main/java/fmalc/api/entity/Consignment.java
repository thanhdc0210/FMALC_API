package fmalc.api.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "consignment")
public class Consignment implements Serializable {
    private static final long serialVersionUID = 1L;

    // Số lượng kiện hàng
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "consignment", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
        private Collection<ConsignmentHistory> consignmentHistories;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_note")
    private String ownerNote;

    // Tổng trọng lượng
    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "status", nullable = false)
    private Integer status;

    @OneToMany(mappedBy = "consignment")
    private Collection<Schedule> schedules;

    @OneToMany(mappedBy = "consignment", cascade = { CascadeType.MERGE })
    private Collection<Location> locations;

    @OneToMany(mappedBy = "consignment", cascade = { CascadeType.MERGE})
    private Collection<Place> places;
}