package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "vehicle")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id")
    private Integer id;

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Collection<Location> locations;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Collection<VehicleStatusDetail> statusReport;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Maintain> maintains;

    @OneToMany(mappedBy = "onwerNotify", cascade = { CascadeType.MERGE })
    private Collection<Notify> notifies;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Schedule> schedules;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Alert> alerts;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<ReportIssue> reportIssues;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private VehicleType vehicleType;

    @Column(name = "kilometer_running", nullable = false)
    private Integer kilometerRunning;

    @Column(name = "current_location", nullable = false)
    private String currentLocation;

    @Column(name = "date_of_manufacture", nullable = false)
    private Date dateOfManufacture;

    /**
     * Biển số xe
     */
    @Column(name = "license_plates", nullable = false)
    private String licensePlates;

    @JoinColumn(name = "vehicle_type_id", nullable = false)
    @ManyToOne
    private VehicleType vehicleTypeId;

}