package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "driver_license")
@Entity
public class DriverLicense implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "license", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<Driver> drivers;

    // Hạng bằng lái
    @Column(name = "license_type", nullable = false)
    private String licenseType;


    @OneToMany(mappedBy = "driver_license", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<VehicleType> vehicleTypes;

}