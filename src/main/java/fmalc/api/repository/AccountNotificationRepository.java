package fmalc.api.repository;

import fmalc.api.entity.AccountNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountNotificationRepository extends JpaRepository<AccountNotification, Integer>, JpaSpecificationExecutor<AccountNotification> {

    @Query("Select an From AccountNotification an Where an.account.username = :username")
    List<AccountNotification> findByUsername(@Param("username") String username);
}
