package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Location> locations;

    @Column(name = "status", nullable = false)
    private Integer status;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Maintain> maintains;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Notify> notifies;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Schedule> schedules;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Alert> alerts;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<ReportIssue> reportIssues;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE})
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "kilometer_running", nullable = false)
    private Integer kilometerRunning;

    @Column(name = "date_of_manufacture", nullable = false)
    private Date dateOfManufacture;

    @Column(name = "weight", nullable = false)
    private Double weight;
    /**
     * Biển số xe
     */
    @Column(name = "license_plates", nullable = false)
    private String licensePlates;

    // Trọng tải của xe

    @Column (name = "vehicle_name", nullable = false)
    private String vehicleName;
}