package fmalc.api.repository;

import fmalc.api.entity.ReportIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportIssueRepository extends JpaRepository<ReportIssue, Integer> {
}
