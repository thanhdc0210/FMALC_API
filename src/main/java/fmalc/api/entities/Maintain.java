package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "maintain")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Maintain implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "image_maintain", nullable = false)
    private String imageMaintain;

    @Column(name = "km_new", nullable = false)
    private Integer kmNew;

    @Column(name = "km_old", nullable = false)
    private Integer kmOld;

    @Column(name = "maintain_date", nullable = false)
    private Date maintainDate;

    @JoinColumn(name = "maintain_type_id", nullable = false)
    @ManyToOne
    private MaintainType maintainTypeId;

    @JoinColumn(name = "vehicle_id", nullable = false)
    @ManyToOne
    private Vehicle vehicleId;
}