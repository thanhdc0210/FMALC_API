package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "maintain")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Maintain implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", insertable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "maintain_type_id", referencedColumnName = "id", insertable = false)
    private MaintainType maintainType;

    @Column(name = "image_maintain", nullable = false)
    private String imageMaintain;

    @Column(name = "km_new", nullable = false)
    private Integer kmNew;

    @Column(name = "km_old", nullable = false)
    private Integer kmOld;

    @Column(name = "maintain_date", nullable = false)
    private Date maintainDate;
}