package fmalc.api.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AccountNotificationKey implements Serializable {
    @Column(name = "account_id")
    Integer accountId;

    @Column(name = "notification_id")
    Integer notificationId;
}
