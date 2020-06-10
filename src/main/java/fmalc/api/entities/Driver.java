package fmalc.api.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "driver")
@Getter
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

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE })
    private Collection<ReportIssue> reportIssues;

    @OneToMany(mappedBy = "driver", cascade = { CascadeType.MERGE })
    private Collection<Alert> alerts;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "driver_status_id", referencedColumnName = "id", nullable = false)
    private DriverStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "driver_license_id", referencedColumnName = "id", nullable = false)
    private DriverLicense license;

     @JoinColumn(name = "account_id", nullable = false)
     @OneToOne
     private Account accountId;

    @Column(name = "identity_no", nullable = false)
    private String identityNo;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
}