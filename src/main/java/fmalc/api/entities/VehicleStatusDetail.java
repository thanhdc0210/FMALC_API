package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "vehicle_status_detail")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VehicleStatusDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", insertable = false, nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "status_id", referencedColumnName = "id", insertable = false, nullable = false)
    private VehicleStatus vehicleStatus;

    @Column(name = "time", nullable = false)
    private Timestamp time;

}