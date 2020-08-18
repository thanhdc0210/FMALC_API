package fmalc.api.controller;

import fmalc.api.dto.*;
import fmalc.api.entity.DayOff;
import fmalc.api.entity.Driver;
import fmalc.api.enums.DayOffEnum;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.repository.DayOffRepository;
import fmalc.api.service.DayOffService;
import fmalc.api.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
@RequestMapping("/api/v1.0/dayOffs")
public class DayOffController {
    @Autowired
    DayOffRepository dayOffRepository;
    @Autowired
    DriverService driverService;
    @Autowired
    DayOffService dayOffService;

    @PostMapping("confirm-dayoff")
    public ResponseEntity confirmDayOff(@RequestBody DayOffDTO dayOffDTO) {
        try {
            boolean result = dayOffService.confirmDayOff(dayOffDTO);
            if (result) {
//                List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = dayOffService.getSchedules(dayOffDTO);
                return ResponseEntity.ok().body(result);
            } else {
                List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = dayOffService.getSchedules(dayOffDTO);
                if (scheduleForConsignmentDTOS.size() < 0) {
                    List<MaintainCheckDTO> maintainCheckDTOS = dayOffService.getListMaintenance(dayOffDTO);
                    if (maintainCheckDTOS.size() > 0) {
                        return ResponseEntity.ok().body(maintainCheckDTOS);
                    }
                } else {
                    return ResponseEntity.ok().body(scheduleForConsignmentDTOS);
                }

            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("detail/{id}")
    public ResponseEntity<DayOffRespsoneDTO> getDetail(@PathVariable("id") int id) {
        try {
            DayOff result = dayOffService.getDetail(id);

            if (result != null) {
                DayOffRespsoneDTO dayOffResponseDTO = new DayOffRespsoneDTO().convertDTO(result);
                return ResponseEntity.ok().body(dayOffResponseDTO);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("cancel-dayoff")
    public ResponseEntity<Boolean> cancelDayOff(@RequestBody DayOffDTO dayOffDTO) {
        try {
            boolean result = dayOffService.cancelDayOff(dayOffDTO);
            if (result) {
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/")
//    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<DayOffResponseDTO> checkDayOffForDriver(@RequestBody DayOffDriverRequestDTO dto) throws ParseException {
        try{
            DayOff dayOff = dayOffService.checkDriverDayOffRequest(dto.getDriverId(), dto.getStartDate(), dto.getEndDate());
            if (dayOff == null) {
                Driver driver = driverService.findById(dto.getDriverId());
                if (driver != null) {
                    DayOff dayOffNew = new DayOff();
                    dayOffNew.setDriver(driver);
                    dayOffNew.setFleetManager(driver.getFleetManager());
                    dayOffNew.setIsApprove(DayOffEnum.WAITING.getValue());
                    dayOffNew.setNote(dto.getContent());
                    if (dto.getType() == NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue()) {
                        java.sql.Date parsedStartDate = new Date(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getStartDate()).getTime());
                        java.sql.Date parsedEndDate = new Date(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getEndDate()).getTime());
                        dayOffNew.setStartDate(parsedStartDate);
                        dayOffNew.setEndDate(parsedEndDate);
                    } else if (dto.getType() == NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue()) {
                        java.sql.Date today = new Date(Calendar.getInstance().getTime().getTime());
                        dayOffNew.setStartDate(today);
                        dayOffNew.setEndDate(today);
                    }
                    dayOffNew = dayOffRepository.save(dayOffNew);
                    if (dayOffNew != null) {
                        return ResponseEntity.ok().body(new DayOffResponseDTO(dayOffNew.getStartDate().toString(), dayOffNew.getEndDate().toString(), 0));
                    } else {
                        return ResponseEntity.badRequest().build();
                    }
                }
                return ResponseEntity.badRequest().build();
            } else {
                //trả day off tồn tại rồi
                DayOffResponseDTO responseDTO = new DayOffResponseDTO();
                responseDTO.setDayOffId(dayOff.getId());
                responseDTO.setEndDate(dayOff.getEndDate().toString());
                responseDTO.setStartDate(dayOff.getStartDate().toString());
                return ResponseEntity.ok().body(responseDTO);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<DayOffResponseDTO> updateDayOff(@PathVariable("id") Integer id, @RequestBody DayOffDriverRequestDTO dto) throws ParseException {
        try {
            DayOff dayOff = dayOffRepository.findById(id).get();
            if (dayOff != null) {
                if (dto.getType() == NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue()) {
                    java.sql.Date parsedStartDate = new Date(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getStartDate()).getTime());
                    java.sql.Date parsedEndDate = new Date(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getEndDate()).getTime());
                    dayOff.setStartDate(parsedStartDate);
                    dayOff.setEndDate(parsedEndDate);
                } else if (dto.getType() == NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue()) {
                    java.sql.Date today = new Date(Calendar.getInstance().getTime().getTime());
                    dayOff.setStartDate(today);
                    dayOff.setEndDate(today);
//                dayOff.setId(id);
                }


            }

            if (dayOff != null) {
                return ResponseEntity.ok().body(new DayOffResponseDTO(dayOff.getStartDate().toString(), dayOff.getEndDate().toString(), dayOff.getId()));
            }
            dayOff = dayOffRepository.save(dayOff);
        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();

    }
}
