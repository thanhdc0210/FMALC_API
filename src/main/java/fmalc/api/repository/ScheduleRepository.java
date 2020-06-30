package fmalc.api.repository;

import fmalc.api.entity.Schedule;
import fmalc.api.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Schedule findScheduleByConsignment(Integer consignmentId);
}
