package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;

@Table(name = "alert")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Alert implements Serializable {
    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "alert_type_id", nullable = false)
    @OneToOne
    private AlertType alertTypeId;

    @Column(name = "content", nullable = false)
    private String content;

    @JoinColumn(name = "driver_id", nullable = false)
    @ManyToOne
    private Driver driverId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    @JoinColumn(name = "vehicle_id", nullable = false)
    @ManyToOne
    private Vehicle vehicleId;

    
}