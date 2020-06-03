package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "driver_license")
@Entity
public class DriverLicense implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "beginning_date", nullable = false)
    private Timestamp beginningDate;

    @Column(name = "expires", nullable = false)
    private Double expires;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @JoinColumn(name = "license_type_id", nullable = false)
    @OneToOne
    private LicenseType licenseTypeId;

    @Column(name = "no", nullable = false)
    private String no;

    
}