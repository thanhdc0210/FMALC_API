package fmalc.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
//    @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
//    private Driver driver;

    /**
     * Thời gian gửi thông báo
     */
    @Column(name = "time", nullable = false)
    private Timestamp time;

    @Column(name = "content", nullable = false)
    private String content;

    // Phân biệt 4 loại notification.
    // Odd-hours alert và Long Idle Times
    // Maintain và schedule
    @Column(name = "type", nullable = false)
    private Integer type;

//    @ManyToMany
//    @JoinTable(name = "read_notification",
//            joinColumns = {@JoinColumn(name = "notification_id")},
//            inverseJoinColumns = {@JoinColumn(name = "account_id")}
//    )
//    private Collection<Account> account;

    @ManyToMany(mappedBy = "notification")
    Collection<AccountNotification> accountNotifications;
}