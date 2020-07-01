package fmalc.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "notification")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Thông báo khi tài xế dừng tại 1 điểm quá lâu, lái xe ngoài giờ làm việc
public class Notification implements Serializable {
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

    /**
     * Thời gian gửi thông báo
     */
    @Column(name = "time", nullable = false)
    private Timestamp time;

    @Column(name = "content", nullable = false)
    private String content;

    // Kiểm tra thông báo đã được đọc hay chưa
    @Column(name = "status", nullable = false)
    private Boolean status;

}