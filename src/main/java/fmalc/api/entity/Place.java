package fmalc.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "place")
public class Place implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "planned_time", nullable = false)
    private Timestamp plannedTime;

    @Column(name = "actual_time")
    private Timestamp actualTime;

    @Column(name = "type", nullable = false)
    private Integer type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "consignment_id", referencedColumnName = "id", nullable = false)
    private Consignment consignment;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_phone")
    private String contactPhone;
}