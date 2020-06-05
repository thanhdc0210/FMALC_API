package fmalc.api.entities;

import java.io.Serializable;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "driver")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "account_id", nullable = false)
    @OneToOne
    private Account accountId;

    @Column(name = "driver_license_id", nullable = false)
    private Integer driverLicenseId;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "identity_no", nullable = false)
    private String identityNo;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "number_phone", nullable = false)
    private String numberPhone;

    
}