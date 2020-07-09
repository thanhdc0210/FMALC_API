package fmalc.api.controller;

import fmalc.api.dto.DetailedScheduleDTO;
import fmalc.api.dto.ScheduleResponseDTO;
import fmalc.api.entity.Schedule;
import fmalc.api.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/schedules")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @GetMapping(value = "driver")
    public ResponseEntity<List<ScheduleResponseDTO>> findByConsignmentStatusAndUsername(@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username){
        List<Schedule> schedules = scheduleService.findByConsignmentStatusAndUsername(status, username);

        if (schedules == null){
            return ResponseEntity.noContent().build();
        }
        List<ScheduleResponseDTO> consignmentResponses = new ArrayList<>(new ScheduleResponseDTO().mapToListResponse(schedules));

        return ResponseEntity.ok().body(consignmentResponses);
    }

        @GetMapping(value = "id/{id}")
    public ResponseEntity<DetailedScheduleDTO> findById(@PathVariable("id") Integer id){
        Schedule schedule = scheduleService.findById(id);
        if (schedule == null || schedule.equals("")){
            return ResponseEntity.noContent().build();
        }
        DetailedScheduleDTO detailedScheduleDTO = new DetailedScheduleDTO(schedule);

        return ResponseEntity.ok().body(detailedScheduleDTO);
    }
}
