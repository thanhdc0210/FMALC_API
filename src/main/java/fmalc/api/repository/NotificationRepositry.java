package fmalc.api.repository;


import fmalc.api.entity.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepositry extends JpaRepository<Notification, Integer> {
    int countAllByStatusFalse();

    List<Notification> findTop4ByStatusIsFalseOrderByIdDesc();

//    @Query(value = "Select id, content, status, time, type, driverId, vehicleId From Notification n " +
//            "Where n.driverId = :id")
    List<Notification> findByDriverId(Integer driver_id);
}
