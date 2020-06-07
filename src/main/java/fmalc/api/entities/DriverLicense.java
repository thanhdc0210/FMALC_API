package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
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
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "beginning_date", nullable = false)
    private Timestamp beginningDate;

    @Column(name = "expires", nullable = false)
    private Double expires;

    @Column(name = "license_type", nullable = false)
    private String licenseType;

    @Column(name = "no", nullable = false)
    private String no;

}