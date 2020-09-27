package fmalc.api.repository;

import fmalc.api.entity.AccountNotification;
import fmalc.api.entity.AccountNotificationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface AccountNotificationRepository extends JpaRepository<AccountNotification, AccountNotificationKey>, JpaSpecificationExecutor<AccountNotification> {

    @Query("Select an From AccountNotification an Where an.account.username = :username " +
            "AND an.notification.time between :startDate and :endDate")
    List<AccountNotification> findByUsernameAndTime(@Param("username") String username,
                    @Param("startDate")Timestamp startDate, @Param("endDate") Timestamp endDate);


    @Query("select an from  AccountNotification  an where  an.account.id=?1 and an.notification.id=?2")
    AccountNotification getAccountNotiByFleetAndNoti(int idFleet, int idNoti);

    List<AccountNotification> findAllByAccount_UsernameAndStatusIsFalseAndNotification_Type(String username, Integer type);

    AccountNotification findByNotification_IdAndAccount_Username(Integer notificationId, String username);

    int countAllByAccount_UsernameAndStatusIsFalseAndNotification_TypeNot(String username, Integer type);

    List<AccountNotification> findTop4ByAccount_UsernameAndStatusIsFalseAndNotification_TypeNot(String username, int type);
}
