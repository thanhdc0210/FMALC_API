package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "vehicle")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "current_location", nullable = false)
    private String currentLocation;

    @Column(name = "date_of_manufacture", nullable = false)
    private Date dateOfManufacture;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "kilometer_running", nullable = false)
    private Integer kilometerRunning;

    /**
     * Biển số xe
     */
    @Column(name = "license_plates", nullable = false)
    private String licensePlates;

    @JoinColumn(name = "vehicle_type_id", nullable = false)
    @ManyToOne
    private VehicleType vehicleTypeId;

    
}