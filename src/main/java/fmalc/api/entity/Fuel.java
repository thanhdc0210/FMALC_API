package fmalc.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fuel")
public class Fuel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "unit_price_at_filling_time", nullable = false)
    private Double unitPriceAtFillingTime;

    @Column(name = "volume", nullable = false)
    private Double volume;

    @Column(name = "km_old", nullable = false)
    private Integer kmOld;

    @Column(name = "filling_date", nullable = false)
    private Date fillingDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "fuel_type_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private FuelType fuelType;
}
