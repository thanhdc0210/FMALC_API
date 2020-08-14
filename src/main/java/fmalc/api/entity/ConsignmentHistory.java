package fmalc.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "consignment_history")
@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class ConsignmentHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
