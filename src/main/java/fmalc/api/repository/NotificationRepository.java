package fmalc.api.repository;


import fmalc.api.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
//    int countAllByAccountNotContainsAndTypeNot(Account account, Integer type);

//    List<Notification> findTop4ByAccountNotContainsAndTypeNotOrderByIdDesc(Account account, Integer type);
    @Query("SELECT n FROM Notification n, AccountNotification  an, Account a where n.id = an.notification.id and a.id=an.account.id and a.id=?1 and n.type=?2")
    Page<Notification> findAllTypeAndAccount(int idAccount, int type, Pageable pa);

    Page<Notification> findAllByTypeOrderByIdDesc(int type, Pageable pa);

    List<Notification> findAllByTypeInOrderByIdDesc(List<Integer> asList);

//    List<Notification> findByDriverId(Integer driver_id);

//    List<Notification> findAllByTypeAndAccountContains(Integer type, Account account);
}
