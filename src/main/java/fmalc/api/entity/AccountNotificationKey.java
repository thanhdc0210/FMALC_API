package fmalc.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AccountNotificationKey implements Serializable {
    @Column
    Integer accountId;

    @Column
    Integer notificationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountNotificationKey that = (AccountNotificationKey) o;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(notificationId, that.notificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, notificationId);
    }
}
