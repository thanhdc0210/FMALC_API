package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;

@Table(name = "notify")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notify implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "content", nullable = false)
    private String content;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @JoinColumn(name = "notify_type_id", nullable = false)
    @ManyToOne
    private NotifyType notifyTypeId;

    /**
     * Thời gian gửi thông báo
     */
    @Column(name = "time", nullable = false)
    private Timestamp time;

    @JoinColumn(name = "vehicle_id", nullable = false)
    @ManyToOne
    private Vehicle vehicleId;

    
}