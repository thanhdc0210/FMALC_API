package fmalc.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Collection;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "status", nullable = false)
    private Integer status;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Maintenance> maintains;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Notification> notifies;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Schedule> schedules;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Alert> alerts;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<ReportIssue> reportIssues;

    @OneToMany(mappedBy = "vehicle", cascade = { CascadeType.MERGE })
    private Collection<Fuel> fuels;

    @Column(name = "kilometer_running", nullable = false)
    private Integer kilometerRunning;

    @Column(name = "date_of_manufacture", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateOfManufacture;

    @Column(name = "weight", nullable = false)
    private Double weight;
    /**
     * Biển số xe
     */
    @Column(name = "license_plates", nullable = false, unique = true)
    private String licensePlates;


    @Column (name = "vehicle_name", nullable = false)
    private String vehicleName; // tên xe

    @Column(name = "average_fuel", nullable = false)
    private Double averageFuel;

    @Column(name = "maximum_capacity", nullable = false)
    private Double maximumCapacity;

    // Loại bằng lái yêu cầu
    @Column(name = "driver_license", nullable = false)
    private Integer driverLicense;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @CreatedDate
    @Column(name = "date_create", nullable = false)
    private Date dateCreate;
}