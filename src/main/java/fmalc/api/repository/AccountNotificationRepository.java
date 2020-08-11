package fmalc.api.repository;

import fmalc.api.entity.AccountNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountNotificationRepository extends JpaRepository<AccountNotification, Integer>, JpaSpecificationExecutor<AccountNotification> {

    @Query("Select an From AccountNotification an Where an.account.username = :username")
    List<AccountNotification> findByUsername(@Param("username") String username);

    List<AccountNotification> findAllByAccount_UsernameAndStatusIsFalseAndNotification_Type(String username, Integer type);

    AccountNotification findByNotification_IdAndAccount_Username(Integer notificationId, String username);

    int countAllByAccount_UsernameAndStatusIsFalseAndNotification_TypeNot(String username, Integer type);

    List<AccountNotification> findTop4ByAccount_UsernameAndStatusIsFalseAndNotification_TypeNot(String username, int type);
}
