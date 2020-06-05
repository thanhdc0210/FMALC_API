package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "driver_status_detail")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DriverStatusDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @JoinColumn(name = "id_driver", nullable = false)
    @ManyToOne
    private Driver idDriver;

    @JoinColumn(name = "id_status", nullable = false)
    @ManyToOne
    private DriverStatus idStatus;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    
}