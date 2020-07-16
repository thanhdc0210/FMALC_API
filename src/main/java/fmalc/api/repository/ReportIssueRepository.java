package fmalc.api.repository;

import fmalc.api.entity.ReportIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ReportIssueRepository extends JpaRepository<ReportIssue, Integer> {

    @Query("select r from ReportIssue r where r.vehicle.id =?1")
    List<ReportIssue> findReportIssueByVehicle(int idVehicle);
}
