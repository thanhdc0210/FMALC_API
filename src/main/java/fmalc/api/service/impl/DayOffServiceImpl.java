package fmalc.api.service.impl;

import fmalc.api.dto.DayOffDTO;
import fmalc.api.dto.MaintainCheckDTO;
import fmalc.api.dto.PlaceResponeDTO;
import fmalc.api.dto.ScheduleForConsignmentDTO;
import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.repository.DayOffRepository;
import fmalc.api.repository.MaintenanceRepository;
import fmalc.api.repository.MaintenanceTypeRepository;
import fmalc.api.repository.NotificationRepository;
import fmalc.api.service.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DayOffServiceImpl implements DayOffService {

    @Autowired
    DayOffRepository dayOffRepository;

    @Autowired
    PlaceService placeService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MaintenanceService maintenanceService;
    @Autowired
    DriverService driverService;

    @Autowired
    AccountService accountService;

    @Autowired
    MaintenanceRepository maintainanceRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    AccountNotificationService accountNotificationService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    ;

    @Override
    public List<Driver> checkDayOffODriver(List<Driver> idDriver, Consignment consignment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Driver> drivers = new ArrayList<>();
        List<DayOff> dayOffs = new ArrayList<>();
        List<Place> places = (List<Place>) consignment.getPlaces();
        boolean flag = true;
        for (int i = 0; i < idDriver.size(); i++) {
            flag = true;
            dayOffs = dayOffRepository.checkDayOffOfDriver(idDriver.get(i).getId());
            if (dayOffs.size() > 0) {
                for (int j = 0; j < dayOffs.size(); j++) {
                    String dateOff = sdf.format(dayOffs.get(j).getStartDate());

                    PlaceResponeDTO placeSchedulePriorityRecei =
                            getPlaceByTypePlaceAndPriority(places, 1, TypeLocationEnum.RECEIVED_PLACE.getValue());

                    List<PlaceResponeDTO> placeConsgimentsPriorityDeli =
                            getPlaceByTypePlace(places, TypeLocationEnum.DELIVERED_PLACE.getValue());

                    if (placeSchedulePriorityRecei != null) {
                        String dateReceiOfConsignment = sdf.format(placeSchedulePriorityRecei.getPlannedTime());
                        if (dateOff.compareTo(dateReceiOfConsignment) >= 1) {
                            PlaceResponeDTO placeSchedulePriorityDeli =
                                    placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), placeConsgimentsPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());
                            if (placeSchedulePriorityDeli != null) {
                                String dateDeliOfConsignment = sdf.format(placeSchedulePriorityDeli.getPlannedTime());
                                if (dateOff.compareTo(dateDeliOfConsignment) >= 1) {

                                } else {
                                    flag = false;
                                }
                            } else {
//
                            }

                        } else if (dateOff.compareTo(dateReceiOfConsignment) <= -1) {
                            String dateEnd = sdf.format(dayOffs.get(j).getEndDate());
                            if (dateEnd.compareTo(dateReceiOfConsignment) >= 1) {
                                flag = false;
                            }
                        }
                    } else {

                    }
                    if (flag == false) {
                        j = dayOffs.size();
                    } else {

                    }
                }
                if (flag == true) {
                    drivers.add(idDriver.get(i));
                }

//                String dateConsignment =
            } else {
                drivers.add(idDriver.get(i));
            }
        }
        return drivers;
    }

    private PlaceResponeDTO getPlaceByTypePlaceAndPriority(List<Place> places, int priority, int type) {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        Place place = new Place();
        for (int i = 0; i < places.size(); i++) {
            if (places.get(i).getPriority() == priority && places.get(i).getType() == type) {
                place = places.get(i);
            }
        }
        if (place != null) {
            placeResponeDTO = placeResponeDTO.convertPlace(place);
        }

        return placeResponeDTO;
    }

    private List<PlaceResponeDTO> getPlaceByTypePlace(List<Place> places, int type) {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        List<PlaceResponeDTO> placeResponeDTOS = new ArrayList<>();
        List<Place> placesResult = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            if (places.get(i).getType() == type) {
                placesResult.add(places.get(i));
            }
        }
        if (places != null) {
            placeResponeDTOS = placeResponeDTO.mapToListResponse(placesResult);
        }

        return placeResponeDTOS;
    }

    @Override
    public void save(DayOff dayOff) {
        dayOffRepository.save(dayOff);
    }

    @SneakyThrows
    @Override
    public boolean confirmDayOff(DayOffDTO dayOffDTO) {
        Driver driver = new Driver();
        driver = driverService.findById(dayOffDTO.getIdDriver());
        FleetManager fleetManager = driver.getFleetManager();
        boolean flag = true;
        flag = checkSchedule(driver.getId(), (dayOffDTO.getDateStart()),(dayOffDTO.getDateEnd()));
        if (flag) {
            flag = findDriverForMaintain(driver, (dayOffDTO.getDateStart()), (dayOffDTO.getDateEnd()));
            if (flag) {
                flag = checkDayOff((dayOffDTO.getDateStart()), driver.getId(), (dayOffDTO.getDateEnd()));
            }
        }
        if (flag) {
            Notification notification = new Notification();
            AccountNotification accountNotification = new AccountNotification();
            notification = notificationRepository.findById(dayOffDTO.getIdNotify()).get();
            notification.getType();
            String note = NotificationTypeEnum.getValueEnumToShow(notification.getType()) +" "+ notification.getContent();
            DayOff dayOff = new DayOff();
            dayOff.setDriver(driver);

            dayOff.setStartDate(new Date(sdf.parse((dayOffDTO.getDateStart())).getTime()));
            dayOff.setEndDate(new Date(sdf.parse((dayOffDTO.getDateEnd())).getTime()));
            dayOff.setFleetManager(fleetManager);
            dayOff.setNote(note);
            
            if (dayOffRepository.save(dayOff) != null) {
                Account account = accountService.findById(fleetManager.getAccount().getId());
                accountNotification = accountNotificationService.findByFleetAndNoti(account.getId(), notification.getId());
                accountNotification.setStatus(true);
                accountNotification = accountNotificationService.save(accountNotification);
            }
        }
        return flag;
    }

    private boolean checkSchedule(int id, String dateStart, String dateEnd) {
//        boolean flag = true;
        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        List<Driver> k = new ArrayList<>();
        java.util.Date result = new java.util.Date();
        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
        scheduleForConsignmentDTOS = checkScheduleForDriver(id);

        boolean flag = true;

        if (scheduleForConsignmentDTOS.size() > 0) {

            for (int j = 0; j < scheduleForConsignmentDTOS.size(); j++) {
                scheduleForLocationDTO = scheduleForConsignmentDTOS.get(j);
                if (scheduleForLocationDTO.getConsignment().getStatus() != ConsignmentStatusEnum.COMPLETED.getValue() ||
                        scheduleForLocationDTO.getConsignment().getStatus() != ConsignmentStatusEnum.CANCELED.getValue()
                ) {
                    List<PlaceResponeDTO> listScheduleDeli =
                            placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                    PlaceResponeDTO placeScheduleRecei =
                            placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());
                    PlaceResponeDTO placeScheduleDeli =
                            placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), listScheduleDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                    String dateRecei = sdf.format(placeScheduleRecei.getPlannedTime());
                    String dateDeli = sdf.format(placeScheduleDeli.getPlannedTime());
                    if (dateStart.compareTo(dateRecei) < 0) {
                        if (dateStart.compareTo(sdf.format(result)) > 0) {
                            if (dateEnd.compareTo(dateRecei) < 0 && dateEnd.compareTo(sdf.format(result)) > 0) {
                                flag = true;
                            }

                        } else {
                            flag = false;
                            j = scheduleForConsignmentDTOS.size();
                        }
                    } else if (dateStart.compareTo(dateRecei) > 0) {
                        if (dateStart.compareTo(dateDeli) > 0 && (dateStart.compareTo(dateDeli) >= dateDeli.compareTo(dateRecei))) {

                            flag = true;
                        } else {
                            flag = false;
                            j = scheduleForConsignmentDTOS.size();
//                            i++;
                        }
                    } else {
                        flag = false;
                        j = scheduleForConsignmentDTOS.size();
//                        i++;
                    }
                    if (flag && j == scheduleForConsignmentDTOS.size() - 1) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }

            }

        } else {
            flag = true;
        }
        return flag;
    }

    private List<ScheduleForConsignmentDTO> checkScheduleForDriver(int idDriver) {
        List<ScheduleForConsignmentDTO> scheduleForLocationDTOS = new ArrayList<>();
        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        List<ScheduleForConsignmentDTO> result = new ArrayList<>();
        boolean flag = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Schedule> Schedules = scheduleService.checkDriverInScheduled(idDriver);
        if (Schedules.size() > 0) {

            scheduleForLocationDTOS = scheduleForLocationDTO.mapToListResponse(Schedules);
            if (scheduleForLocationDTOS.size() > 0) {
                for (int i = 0; i < scheduleForLocationDTOS.size(); i++) {
                    flag = true;
                    PlaceResponeDTO deliveryDetail = new PlaceResponeDTO();
                    deliveryDetail = placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTOS.get(i).getConsignment().getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());
                    if (deliveryDetail.getPlannedTime() != null) {
                        String datePlace = sdf.format(deliveryDetail.getPlannedTime());
                        String dateNow = sdf.format(new java.util.Date());
                        if (dateNow.compareTo(datePlace) <= 0) {
                            flag = false;
//                            id.add(scheduleForLocationDTOS.get(i).getId());
                        }
                    } else {
                        flag = false;
//                        id.add(scheduleForLocationDTOS.get(i).getId());
                    }
                    if (flag) {
                        result.add(scheduleForLocationDTOS.get(i));
                    }
                }

            }


        }
        return scheduleForLocationDTOS;
    }

    private boolean checkDateMaintain(String dateS, List<MaintainCheckDTO> maintainCheckDTO, String dateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = false;
        if (maintainCheckDTO.size() > 0) {
            for (int i = 0; i < maintainCheckDTO.size(); i++) {
                String dateMaintain = sdf.format(maintainCheckDTO.get(i).getPlannedMaintainDate());
                if (dateS.compareTo(dateMaintain) > 0) {
                    if (dateEnd.compareTo(dateMaintain) > 0) {
                        flag = true;
                    }

                } else {
                    Maintenance maintenance = maintainanceRepository.findById(maintainCheckDTO.get(i).getId()).get();


                }
                if (!flag) {
                    flag = false;
                    i = maintainCheckDTO.size();
                }
            }
        } else {
            flag = true;
        }

        return flag;
    }

    private boolean findDriverForMaintain(Driver driver, String date, String dateEnd) {
        boolean flag = true;
        flag = checkDateMaintain(date, checkMaintainForDriver(driver.getId()), dateEnd);
        if (flag) {
            flag = checkSchedule(driver.getId(), date, dateEnd);
            if (flag) {
                flag = checkDayOff(date, driver.getId(), dateEnd);
            }
        }

        return flag;
    }


    private List<MaintainCheckDTO> checkMaintainForDriver(int idDriver) {

        List<Maintenance> maintenances = maintainanceRepository.findByDriver(idDriver);
        if (!maintenances.isEmpty()) {
            List<MaintainCheckDTO> maintainCheckDTOs = new MaintainCheckDTO().mapToListResponse(maintenances);


            maintainCheckDTOs.sort(Comparator.comparing(MaintainCheckDTO::getActualMaintainDate));

            Date date = new Date(System.currentTimeMillis());
//            List<Integer> id = maintainCheckDTOs.stream()
//                    .filter(x -> x.getStatus() == 1)
//                    .map(MaintainCheckDTO::getId)
//                    .collect(Collectors.toList());
//            maintainCheckDTOs.removeIf(x -> id.contains(x.getId()));
            int t = 0;
            for (int i = 0; i < maintainCheckDTOs.size(); i++) {
                t = i;
                if (maintainCheckDTOs.get(i).getActualMaintainDate() != null) {
                    if (date.after(maintainCheckDTOs.get(i).getActualMaintainDate())) {
                        maintainCheckDTOs.remove(maintainCheckDTOs.get(i));
                        i = t;
                    }
                } else {
                    if (date.after(maintainCheckDTOs.get(i).getPlannedMaintainDate())) {
                        maintainCheckDTOs.remove(maintainCheckDTOs.get(i));
                        i = t;
                    }
                }
            }

            if (!maintainCheckDTOs.isEmpty()) {
                return maintainCheckDTOs;
            }

        }
        return new ArrayList<>();
    }

    private boolean checkDayOff(String dates, int id, String dateEndPlane) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<DayOff> dayOffs = new ArrayList<>();
        boolean flag = false;
        dayOffs = dayOffRepository.checkDayOffOfDriver(id);
        if (dayOffs.size() > 0) {
            for (int j = 0; j < dayOffs.size(); j++) {
                String dateOff = sdf.format(dayOffs.get(j).getStartDate());

                if (dateOff.compareTo(dates) >= 1) {
                    if (dateOff.compareTo(dateEndPlane) >= 1) {
                        flag = true;
                    }

                } else if (dates.compareTo(dateOff) >= 1) {
                    String dateEnd = sdf.format(dayOffs.get(j).getEndDate());
                    if (dateEnd.compareTo(dates) >= 1) {
                        flag = false;
                    } else if (dateEnd.compareTo(dates) <= -1) {
//                        if(dateEnd.compareTo(dateEndPlane))
                        flag = true;
                    }
                }

                if (flag == false) {
                    j = dayOffs.size();
                } else {

                }
            }
        } else {
            flag = true;
        }
        return flag;
    }
}
