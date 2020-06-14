package fmalc.api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fleet_manager")
public class FleetManager {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
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
}
