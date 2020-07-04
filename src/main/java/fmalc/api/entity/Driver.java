package fmalc.api.entity;

import java.io.Serializable;
import java.util.Collection;
import java.sql.Date;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "driver")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "createBy", cascade = { CascadeType.MERGE })
    private Collection<ReportIssue> reportIssues;

    @OneToMany(mappedBy = "updateBy", cascade = { CascadeType.MERGE })
    private Collection<ReportIssue> reportIssue;

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE })
    private Collection<Alert> alerts;

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE })
    private Collection<Schedule> schedules;

    @Column(name = "status", nullable = false)
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "fleet_manager_id", referencedColumnName = "id", nullable = false)
    private FleetManager fleetManager;

     @JoinColumn(name = "account_id", referencedColumnName = "id",nullable = false)
     @OneToOne
     private Account account;

    @Column(name = "identity_no", nullable = false)
    private String identityNo; // Số trên CMND

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "no", nullable = false)
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

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE })
    private Collection<Maintenance> maintains;

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE})
    private Collection<DayOff> dayOffs;
}