package fmalc.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Table(name = "alert")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
// Tài xế báo cáo về khi có sự cố xảy ra trên đường
public class Alert implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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