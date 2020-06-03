package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;

@Table(name = "vehicle_status_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VehicleStatusDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @JoinColumn(name = "id_status", nullable = false)
    @ManyToOne
    private VehicleStatus idStatus;

    @JoinColumn(name = "id_vehicle", nullable = false)
    @ManyToOne
    private Vehicle idVehicle;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    
}