package fmalc.api.service;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.Driver;

import java.util.*;

public interface DayOffService {
    List<Driver> checkDayOffODriver(List<Driver> idDriver, Consignment consignment);
}
