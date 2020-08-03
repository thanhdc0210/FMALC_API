package fmalc.api.repository;

import fmalc.api.entity.ConsignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsignmentHistoryRepository extends JpaRepository<ConsignmentHistory, Integer> {
}
