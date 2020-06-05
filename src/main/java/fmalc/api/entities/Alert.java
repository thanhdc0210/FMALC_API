package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "alert")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Alert implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "content", nullable = false)
    private String content;

    @JoinColumn(name = "driver_id", nullable = false)
    @ManyToOne
    private Driver driverId;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    @JoinColumn(name = "vehicle_id", nullable = false)
    @ManyToOne
    private Vehicle vehicleId;

    
}