package fmalc.api.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "account_notification")
public class AccountNotification {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    AccountNotificationKey id;

    @ManyToOne
    @MapsId("account_id")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("notification_id")
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @Column(name = "status", nullable = false)
    private Boolean status;
}
