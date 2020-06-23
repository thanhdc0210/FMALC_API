package fmalc.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "notify")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notify implements Serializable {
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
    @JoinColumn(name = "notify_type_id", referencedColumnName = "id", nullable = false)
    private NotifyType notifyType;

    /**
     * Thời gian gửi thông báo
     */
    @Column(name = "time", nullable = false)
    private Timestamp time;

    @Column(name = "content", nullable = false)
    private String content;

}