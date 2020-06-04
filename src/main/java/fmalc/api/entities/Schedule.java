package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule")
@Entity
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @JoinColumn(name = "consignment_id", nullable = false)
    @ManyToOne
    private Consignment consignmentId;

    @Column(name = "drive_date", nullable = false)
    private Date driveDate;

    @JoinColumn(name = "driver_id", nullable = false)
    @ManyToOne
    private Driver driverId;

    @Column(name = "image_consignment", nullable = false)
    private String imageConsignment;

    /**
     * Lý do cancel
     */
    @Column(name = "note")
    private String note;

    @JoinColumn(name = "vehicle_id", nullable = false)
    @ManyToOne
    private Vehicle vehicleId;

    
}