package fmalc.api.dto;

import fmalc.api.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectToSortForSchedule {
    private Schedule schedule;
    private Timestamp plannedTime;
    private Timestamp actualTime;
}
