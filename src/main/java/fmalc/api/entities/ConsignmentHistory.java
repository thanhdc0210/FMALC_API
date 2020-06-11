package fmalc.api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "consignment_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentHistory {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy = GenerationType.AUTO)
    // @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    // @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private Integer id;

    // Thời gian cập nhật status của consignment
    @Column(name = "time", nullable = false)
    private Date time;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "consignment_id", referencedColumnName = "id", nullable = false)
    private Consignment consignment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "fleet_manager_id", referencedColumnName = "id", nullable = false)
    private FleetManager fleetManager;
}
