package fmalc.api.repository;


import fmalc.api.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
//    int countAllByAccountNotContainsAndTypeNot(Account account, Integer type);

//    List<Notification> findTop4ByAccountNotContainsAndTypeNotOrderByIdDesc(Account account, Integer type);


    List<Notification> findAllByTypeOrderByIdDesc(int type);

    List<Notification> findAllByTypeInOrderByIdDesc(List<Integer> asList);

//    List<Notification> findByDriverId(Integer driver_id);

//    List<Notification> findAllByTypeAndAccountContains(Integer type, Account account);
}
