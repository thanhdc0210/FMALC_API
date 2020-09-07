package fmalc.api.repository;

import fmalc.api.entity.DayOff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;


@Repository
public interface DayOffRepository extends JpaRepository<DayOff, Integer> {
    @Query("select do from DayOff do where do.driver.id = ?1 and do.isApprove=?2")
    List<DayOff> checkDayOffOfDriver (int idDriver, int status);

    @Query("select do from DayOff  do where  do.driver.id=?1 and do.isApprove=?2 and do.startDate=?3 and do.endDate=?4")
    DayOff getDayOffExistByDate(int idDriver, Boolean isApprove, Date start, Date end );

    @Query("select do from DayOff  do where  do.driver.id=?1 and do.startDate=?2 and do.endDate=?3")
    DayOff getDayOffExistByDateIsApprove(int idDriver,Date start, Date end );

    List<DayOff> findByDriverIdAndIsApprove(Integer driverId, Integer isApprove);

    @Query("select do from DayOff do, Driver  d, Account a, FleetManager f where " +
            "do.driver.id=d.id and a.id=f.account.id and d.fleetManager.id =f.id and a.username=?1")
    Page findByRole(String username, Pageable pa);
    @Query("select do from DayOff do ")
    Page findAllByOrderByIdDesc(Pageable pa);

//    @Query("select do from DayOff do where do.driver.id = ?1 and do.endDay >= ?2")
//    Boolean checkDayOffEndDriver (int idDriver, Date date);

}
