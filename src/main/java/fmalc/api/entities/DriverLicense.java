package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "driver_license")
@Entity
public class DriverLicense implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "license", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Collection<Driver> drivers;

    @Column(name = "beginning_date", nullable = false)
    private Timestamp beginningDate;

    @Column(name = "expires", nullable = false)
    private Timestamp expires;

    @Column(name = "license_type", nullable = false)
    private String licenseType;

    @Column(name = "no", nullable = false)
    private String no;

}