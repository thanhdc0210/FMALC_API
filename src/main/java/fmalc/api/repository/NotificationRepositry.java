package fmalc.api.repository;


import fmalc.api.entity.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepositry extends JpaRepository<Notification, Integer> {
    int countAllByStatusFalse();
}
