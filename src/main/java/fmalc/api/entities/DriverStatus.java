package fmalc.api.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "driver_status")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DriverStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "status", cascade = { CascadeType.MERGE })
    private Collection<Driver> drivers;

    @Column(name = "status", nullable = false)
    private String status;
}