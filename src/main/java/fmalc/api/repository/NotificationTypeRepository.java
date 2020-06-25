package fmalc.api.repository;

import fmalc.api.entity.NotifyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotifyType, Integer> {
    NotifyType findById(int id);
}
