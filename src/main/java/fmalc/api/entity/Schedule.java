package fmalc.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule")
@Entity
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "consignment_id", referencedColumnName = "id", nullable = false)
    private Consignment consignment;

    @Column(name = "image_consignment", length = 500)
    private String imageConsignment;

    /**
     * Lý do cancel
     */


    @Column(name = "is_approve", nullable = false)
    private Boolean isApprove;

    @OneToMany(mappedBy = "schedule", cascade = { CascadeType.MERGE })
    private Collection<Location> locations;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "inheritance", referencedColumnName = "id")
    private Schedule inheritance;

    public Integer getMonthForReport(){
        return consignment.getStartDateForReport().getMonth();
    }
    public Timestamp getTimeStampForReport(){
        return consignment.getStartDateForReport();
    }
}