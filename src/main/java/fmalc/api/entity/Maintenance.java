package fmalc.api.entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "maintenance")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Maintenance implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "maintain_type_id", referencedColumnName = "id", nullable = false)
    private MaintainType maintainType;

    @Column(name = "image_maintain")
    private String imageMaintain;

    @Column(name = "km_old")
    private Integer kmOld;

    @Column(name = "planned_maintain_date", nullable = false)
    private Date plannedMaintainDate;

    @Column(name = "actual_maintain_date")
    private Date actualMaintainDate;

    @Column(name = "status", nullable = true)
    private Boolean status;

}