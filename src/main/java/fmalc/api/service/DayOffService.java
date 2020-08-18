package fmalc.api.service;

import fmalc.api.dto.DayOffDTO;
import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.dto.MaintainReponseDTO;
import fmalc.api.dto.ScheduleForConsignmentDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.DayOff;
import fmalc.api.entity.Driver;
import java.util.List;


public interface DayOffService {
    List<Driver> checkDayOffODriver(List<Driver> idDriver, Consignment consignment);

    DayOff getDayOffApprove(DayOffDTO dayOffDTO);
    void save(DayOff dayOff);
    List<DayOff> getDayOffOfDriverIsApprove(int idDriver);
    boolean confirmDayOff(DayOffDTO dayOffDTO);
    List<ScheduleForConsignmentDTO> getSchedules(DayOffDTO dayOffDTO);
    List<MaintainCheckDTO> getListMaintenance(DayOffDTO dayOffDTO);
    boolean cancelDayOff(DayOffDTO dayOffDTO);
    DayOff getDetail(int id);
    DayOff checkDriverDayOffRequest(Integer driverId, String startDate, String endDate);

}
