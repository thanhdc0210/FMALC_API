package fmalc.api.service;

import fmalc.api.dto.ScheduleForLocationDTO;
import fmalc.api.entity.Schedule;
import org.springframework.stereotype.Service;


public interface ScheduleService {
    ScheduleForLocationDTO getScheduleByConsignmentId(int id);
    Schedule createSchedule(Schedule schedule);
}
