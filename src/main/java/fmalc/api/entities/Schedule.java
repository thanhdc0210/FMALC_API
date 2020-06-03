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
    @JoinColumn(name = "consignment_id", insertable = false, nullable = false)
    @ManyToOne
    private Consignment consignmentId;

    @Column(name = "drive_date", nullable = false)
    private Date driveDate;

    @Id
    @JoinColumn(insertable = false, name = "driver_id", nullable = false)
    @ManyToOne
    private Driver driverId;

    @Column(name = "image_consignment", nullable = false)
    private String imageConsignment;

    /**
     * Lý do cancel
     */
    @Column(name = "note")
    private String note;

    @Id
    @JoinColumn(name = "vehicle_id", insertable = false, nullable = false)
    @ManyToOne
    private Vehicle vehicleId;

    
}