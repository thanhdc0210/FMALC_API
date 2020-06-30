package fmalc.api.service;

import fmalc.api.entity.Schedule;
import org.springframework.stereotype.Service;


public interface ScheduleService {
    Schedule getScheduleByConsignmentId( Integer id);
}
