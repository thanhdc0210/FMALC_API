package fmalc.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "fuel_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FuelType {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "fuelType", cascade = { CascadeType.MERGE })
    private Collection<Fuel> fuels;
}
