package fmalc.api.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "vehicle_type")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VehicleType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "vehicleType", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @OrderBy("id")
    private Set<Vehicle> vehicles;

    @Column(name = "average_fuel", nullable = false)
    private Double averageFuel;

    @Column(name = "maximum_capacity", nullable = false)
    private Double maximumCapacity;

    @Column(name = "vehicle_type_name", nullable = false)
    private String vehicleTypeName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "driver_license_id", referencedColumnName = "id", nullable = false)
    private DriverLicense driver_license;
}