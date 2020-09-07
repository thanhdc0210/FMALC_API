package fmalc.api.repository;

import fmalc.api.entity.ReportIssue;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public interface ReportIssueRepository extends JpaRepository<ReportIssue, Integer>, PagingAndSortingRepository<ReportIssue, Integer> {

    // Kiểm tra với tài xế A đã làm report-issue cho phương tiện A vào ngày hôm nay chưa.
    // Nếu có rồi thì update lại content sự cố
    @Query("SELECT rs FROM ReportIssue rs WHERE rs.createdBy.account.username = :username " +
            "AND rs.vehicle.licensePlates = :licensePlates " +
            "AND rs.createTime BETWEEN :startDate AND :current")
    ArrayList<ReportIssue> findByUsernameAndLicensePlates(@Param("username") String username,
                                                          @Param("licensePlates") String licensePlates, @Param("startDate") Timestamp startDate,
                                                          @Param("current") Timestamp current);

    @Query("select r from ReportIssue r where r.vehicle.id =?1")
    List<ReportIssue> findReportIssueByVehicle(int idVehicle);

    @Query("select r from ReportIssue r, Account a, FleetManager f, Driver d where " +
            " a.id =f.account.id and r.createdBy.id=d.id and d.fleetManager.id = f.id" +
            " and a.username=?1")
    Page<ReportIssue> getAllByUsername(String username, Pageable pa);

    @Query("select r from ReportIssue r,  Driver d where " +
            "  r.createdBy.id=d.id " +
            " ")
    Page<ReportIssue> getAllByAdmin( Pageable pa);
}
