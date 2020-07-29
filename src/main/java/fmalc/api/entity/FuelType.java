package fmalc.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "fuelType", cascade = { CascadeType.MERGE })
    @JsonIgnore
    private Collection<Fuel> fuels;
}
