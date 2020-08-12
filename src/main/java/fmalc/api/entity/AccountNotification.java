package fmalc.api.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account_notification")
public class AccountNotification {

    private static final long serialVersionUID = 1L;

    //AccountNotificationPK accountNotificationPK;
    @EmbeddedId
    AccountNotificationKey id;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("notificationId")
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @Column(name = "status", nullable = false)
    private Boolean status;
}
