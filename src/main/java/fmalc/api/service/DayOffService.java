package fmalc.api.service;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.DayOff;
import fmalc.api.entity.Driver;

import java.text.ParseException;
import java.util.List;

public interface DayOffService {
    List<Driver> checkDayOffODriver(List<Driver> idDriver, Consignment consignment);

    void save(DayOff dayOff);

    DayOff checkDriverDayOffRequest(Integer driverId, String startDate, String endDate);

}
