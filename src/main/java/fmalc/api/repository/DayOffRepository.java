package fmalc.api.repository;

import fmalc.api.entity.DayOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.sql.Date;


@Repository
public interface DayOffRepository extends JpaRepository<DayOff, Integer> {
    @Query("select do from DayOff do where do.driver.id = ?1")
    List<DayOff> checkDayOffOfDriver (int idDriver);

//    @Query("select do from DayOff do where do.driver.id = ?1 and do.endDay >= ?2")
//    Boolean checkDayOffEndDriver (int idDriver, Date date);

}
