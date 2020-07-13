package fmalc.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "location")
@Entity
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
        private double longitude;

    /**
     * Thời gian khi xe dừng tại 1 điểm
     */
    @Column(name = "time", nullable = false)
    private Timestamp time;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "schedule_id", referencedColumnName = "id", nullable = false)
    private Schedule schedule;

    @Column(name = "address", nullable = false)
    private String address;

}