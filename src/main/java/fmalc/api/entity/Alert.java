package fmalc.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "alert")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Alert implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
    private Driver driver;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "content", nullable = false)
    private String content;

    // Xác định xem thông báo đã được đọc hay chưa
    @Column(name = "status", nullable = false)
    private Boolean status;
}