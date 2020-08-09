package fmalc.api.repository;


import fmalc.api.entity.Account;
import fmalc.api.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    int countAllByAccountNotContains(Account account);

    List<Notification> findTop4ByAccountNotContains(Account account);

    List<Notification> findAllByTypeOrderByIdDesc(int type);

    List<Notification> findByDriverId(Integer driver_id);

    List<Notification> findAllByTypeAndAccountNotContains(Integer type, Account account);
}
