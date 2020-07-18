package fmalc.api.repository;

import fmalc.api.entity.ReportIssue;
import fmalc.api.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public interface ReportIssueRepository extends JpaRepository<ReportIssue, Integer> {

    // Kiểm tra với tài xế A đã làm report-issue cho phương tiện A vào ngày hôm nay chưa
    @Query("SELECT rs FROM ReportIssue rs WHERE rs.createdBy.account.username = :username " +
            "AND rs.vehicle.licensePlates = :licensePlates " +
            "AND rs.createTime BETWEEN :startDate AND :current")
    ArrayList<ReportIssue> findByUsernameAndLicensePlates(@Param("username") String username,
                                                          @Param("licensePlates") String licensePlates, @Param("startDate") Timestamp startDate,
                                                          @Param("current") Timestamp current);
}
