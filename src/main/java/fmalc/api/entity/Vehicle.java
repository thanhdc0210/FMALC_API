package fmalc.api.entity;

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

    // Trọng tải của xe
//    @Column(name = "weight", nullable = false)
//    private Double weight;

    // Loại bằng lái yêu cầu
    @Column(name = "driver_license", nullable = false)
    private Integer driverLicense;
}