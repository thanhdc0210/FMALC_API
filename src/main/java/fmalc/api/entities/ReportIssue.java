package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "report_issue")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReportIssue implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @JoinColumn(name = "inspection_id", nullable = false)
    @ManyToOne
    private Inspection inspectionId;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    @JoinColumn(name = "vehicle_id", nullable = false)
    @ManyToOne
    private Vehicle vehicleId;

    @JoinColumn(name = "driver_id", nullable = false)
    @ManyToOne
    private Driver driverId;

    
}