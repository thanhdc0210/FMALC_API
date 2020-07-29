package fmalc.api.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule")
@Entity
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "consignment_id", referencedColumnName = "id", nullable = false)
    private Consignment consignment;

    @Column(name = "image_consignment", length = 500)
    private String imageConsignment;

    /**
     * Lý do cancel
     */
    @Column(name = "note")
    private String note;

    @Column(name = "is_approve", nullable = false)
    private Boolean isApprove;

    @OneToMany(mappedBy = "schedule", cascade = { CascadeType.MERGE })
    private Collection<Location> locations;

    @OneToOne
    @JoinColumn(name = "inheritance", referencedColumnName = "id")
    private Schedule schedule;
}