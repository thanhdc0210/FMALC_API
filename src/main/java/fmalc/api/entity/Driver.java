package fmalc.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;

@Table(name = "driver")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "createdBy", cascade = { CascadeType.MERGE }, fetch=FetchType.EAGER)
    private Collection<ReportIssue> reportIssues;

    @OneToMany(mappedBy = "updatedBy", cascade = { CascadeType.MERGE })
    private Collection<ReportIssue> reportIssue;

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE })
    private Collection<Alert> alerts;

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE })
    private Collection<Schedule> schedules;

    @Column(name = "status", nullable = false)
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "fleet_manager_id", referencedColumnName = "id", nullable = false)
    private FleetManager fleetManager;

     @JoinColumn(name = "account_id", referencedColumnName = "id",nullable = false)
     @OneToOne
     private Account account;

    @Column(name = "identity_no", nullable = false, unique = true)
    private String identityNo; // Số trên CMND

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "no", nullable = false, unique = true)
    private String no; // Số bằng lái

    @Column(name = "license_expires", nullable = false)
    private Date licenseExpires;

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE })
    private Collection<Notification> notifies;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    // Loại bằng lái của tài xế
    @Column(name = "driver_license", nullable = false)
    private Integer driverLicense;

    @Column(name = "working_hour")
    private Float workingHour;

    @Column(name = "image", length = 500)
    private String image;

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE })
    private Collection<Maintenance> maintains;

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE})
    private Collection<DayOff> dayOffs;

    @Column(name = "token_device")
    private String tokenDevice;
}