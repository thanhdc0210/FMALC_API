package fmalc.api.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

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
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
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

}