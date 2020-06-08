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
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", insertable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "status_id", referencedColumnName = "id", insertable = false)
    private VehicleStatus vehicleStatus;

    @Column(name = "time", nullable = false)
    private Timestamp time;

}