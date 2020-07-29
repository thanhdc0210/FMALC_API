package fmalc.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fleet_manager")
public class FleetManager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "account_id", nullable = false)
    @OneToOne
    private Account account;

    @Column(name = "identity_no", nullable = false)
    private String identityNo;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "fleetManager", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<ConsignmentHistory> consignmentHistories;

    @OneToMany(mappedBy = "fleetManager", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<Driver> drivers;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "image", length = 500)
    private String image;

    @OneToMany(mappedBy = "fleetManager", cascade = { CascadeType.MERGE})
    private Collection<DayOff> dayOffs;
}
