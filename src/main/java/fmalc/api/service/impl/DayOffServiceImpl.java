package fmalc.api.service.impl;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.DayOffEnum;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.repository.DayOffRepository;
import fmalc.api.repository.MaintenanceRepository;
import fmalc.api.repository.NotificationRepository;
import fmalc.api.service.*;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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



    private DayOff checkDayOff(DayOffDTO dayOffDTO){
        DayOff dayOff = new DayOff();
        try {
            dayOff=  dayOffRepository.getDayOffExistByDate(dayOffDTO.getIdDriver(),false,new Date(sdf.parse(dayOffDTO.getDateStart()).getTime()),new Date(sdf.parse(dayOffDTO.getDateEnd()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  dayOff;
    }
    @Override
    public List<Driver> checkDayOffODriver(List<Driver> idDriver, Consignment consignment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Driver> drivers = new ArrayList<>();
        List<DayOff> dayOffs = new ArrayList<>();
        List<Place> places = (List<Place>) consignment.getPlaces();
        boolean flag = true;
        for (int i = 0; i < idDriver.size(); i++) {
            flag = true;
            dayOffs = dayOffRepository.checkDayOffOfDriver(idDriver.get(i).getId(), DayOffEnum.APPROVED.getValue());
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
                                    getPlaceByTypePlaceAndPriority(places, placeConsgimentsPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());
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

    @Override
    public DayOff getDayOffApprove(DayOffDTO dayOffDTO) {
        int id = dayOffDTO.getIdDriver();
        String dates = (dayOffDTO.getDateStart());
        String dateEndPlane = (dayOffDTO.getDateEnd());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<DayOff> dayOffs = new ArrayList<>();
        DayOff result = new DayOff();
        boolean flag = false;
        dayOffs = dayOffRepository.checkDayOffOfDriver(id, DayOffEnum.APPROVED.getValue());
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
                        result=dayOffs.get(j);
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
        return result;
//        return null;
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

    @Override
    public List<DayOff> getDayOffOfDriverIsApprove(int idDriver) {
        return dayOffRepository.findByDriverIdAndIsApprove(idDriver,DayOffEnum.APPROVED.getValue());
    }


    @Override
    public boolean confirmDayOff(DayOffDTO dayOffDTO) {
        Driver driver = new Driver();
        driver = driverService.findById(dayOffDTO.getIdDriver());
//        FleetManager fleetManager = driver.getFleetManager();
        boolean flag = true;

            flag = checkSchedule(driver.getId(), dayOffDTO.getDateStart(),dayOffDTO.getDateEnd());
            if (flag) {
                flag =  flag = checkDateMaintain((dayOffDTO.getDateStart()), checkMaintainForDriver(driver.getId()),  (dayOffDTO.getDateEnd()));
                if (flag) {
                    flag = checkDayOff((dayOffDTO.getDateStart()), driver.getId(), (dayOffDTO.getDateEnd()));
                }
            }
//        }
        if (flag) {
//            notification.getType();
//            String note = NotificationTypeEnum.getValueEnumToShow(notification.getType()) +" "+ notification.getContent();
            DayOff dayOff = new DayOff();

            dayOff = dayOffRepository.findById(dayOffDTO.getId()).get();
            if(dayOff!=null){
                dayOff.setIsApprove(DayOffEnum.APPROVED.getValue());


                if (dayOffRepository.save(dayOff) != null) {

                }
            }else{

               flag = false;

//
            }

        }else{
            flag= false;
        }
        return flag;
    }

    @Override
    public boolean cancelDayOff(DayOffDTO dayOffDTO) {
        Driver driver = new Driver();
        driver = driverService.findById(dayOffDTO.getIdDriver());
        boolean flag = true;
        DayOff dayOff = dayOffRepository.findById(dayOffDTO.getId()).get();
        if(dayOff==null){

            flag = false;


        }else{
            dayOff.setIsApprove(DayOffEnum.REJECTED.getValue());
            dayOff = dayOffRepository.save(dayOff);

        }
        return flag;
    }

    @Override
    public DayOff getDetail(int id) {
        return dayOffRepository.findById(id).get();
    }

    @Override
    public List<ScheduleForConsignmentDTO> getSchedules(DayOffDTO dayOffDTO){
        int id = dayOffDTO.getIdDriver();
        String dateStart = dayOffDTO.getDateStart();
        String dateEnd = dayOffDTO.getDateEnd();
        List<ScheduleForConsignmentDTO> result = new ArrayList<>();
        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        List<Driver> k = new ArrayList<>();
        java.util.Date now = new java.util.Date();
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
                        if (dateStart.compareTo(sdf.format(now)) > 0) {
                            if (dateEnd.compareTo(dateRecei) < 0 && dateEnd.compareTo(sdf.format(result)) > 0) {
                                flag = true;
                            }

                        } else {
                            flag = false;
//                            j = scheduleForConsignmentDTOS.size();
                        }
                    } else if (dateStart.compareTo(dateRecei) > 0) {
                        if (dateStart.compareTo(dateDeli) > 0 && (dateStart.compareTo(dateDeli) >= dateDeli.compareTo(dateRecei))) {

                            flag = true;
                        } else {
                            flag = false;
//                            j = scheduleForConsignmentDTOS.size();
//                            i++;
                        }
                    } else {
                        flag = false;
//                        j = scheduleForConsignmentDTOS.size();
//                        i++;
                    }
                    if (flag) {

                    } else {
                        result.add(scheduleForConsignmentDTOS.get(j));
                    }
                }

            }

        } else {
            flag = true;
        }
        return result;
    }

    @Override
    public List<MaintainCheckDTO> getListMaintenance(DayOffDTO dayOffDTO) {
        List<MaintainCheckDTO> maintainCheckDTOS = checkMaintainForDriver(dayOffDTO.getIdDriver());
        List<MaintainCheckDTO> result = new ArrayList<>();
        for(int i=0; i< maintainCheckDTOS.size(); i++){
            if(checkDateAMaintain(dayOffDTO.getDateStart(),maintainCheckDTOS.get(i),dayOffDTO.getDateEnd())){

            }else{
                result.add(maintainCheckDTOS.get(i));
            }
        }
        return result;
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
                if (scheduleForLocationDTO.getConsignment().getStatus() != ConsignmentStatusEnum.COMPLETED.getValue() &&
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
                if(maintainCheckDTO.get(i).getActualMaintainDate()!=null){
                    String dateMaintain = sdf.format(maintainCheckDTO.get(i).getActualMaintainDate());
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
                }else{
                    flag =true;
                }
            }
        } else {
            flag = true;
        }

        return flag;
    }


    private boolean checkDateAMaintain(String dateS, MaintainCheckDTO maintainCheckDTO, String dateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = false;
//        if (maintainCheckDTO.size() > 0) {
//            for (int i = 0; i < maintainCheckDTO.size(); i++) {
        if(maintainCheckDTO.getActualMaintainDate()!=null){
            String dateMaintain = sdf.format(maintainCheckDTO.getActualMaintainDate());
            if (dateS.compareTo(dateMaintain) > 0) {
                if (dateEnd.compareTo(dateMaintain) > 0) {
                    flag = true;
                }

            }
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
            int t = 0;
            for (int i = 0; i < maintainCheckDTOs.size(); i++) {
                t = i;
                if (maintainCheckDTOs.get(i).getActualMaintainDate() != null) {
                    if (date.after(maintainCheckDTOs.get(i).getActualMaintainDate())) {
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
        dayOffs = dayOffRepository.checkDayOffOfDriver(id, DayOffEnum.APPROVED.getValue());
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

//    private DayOff checkDadyOff(String dates, int id, String dateEndPlane) {
//
//    }

    @Override
    public DayOff checkDriverDayOffRequest(Integer driverId, String startDate, String endDate) {
       List<DayOff> dayOffList = dayOffRepository.findByDriverIdAndIsApprove(driverId,DayOffEnum.WAITING.getValue());
//        DateTimeFormatter formatDate = DateTimeFormat.forPattern("dd-MM-yyyy");
        DateTimeFormatter formatOut = DateTimeFormat.forPattern("yyyy-MM-dd");
        dayOffList.sort(Comparator.comparing(DayOff::getStartDate));
        DateTime start = formatOut.parseDateTime(reverse(startDate)).withTimeAtStartOfDay();
        DateTime end = formatOut.parseDateTime(reverse(endDate)).millisOfDay().withMaximumValue();
        Interval interval = new Interval(start,end);

        for(DayOff dayOff : dayOffList){
            DateTime s = new DateTime(dayOff.getStartDate().getTime()).withTimeAtStartOfDay();
            DateTime e = new DateTime(dayOff.getEndDate().getTime()).millisOfDay().withMaximumValue();
            Interval dbInterval = new Interval(s,e);
            if (interval.overlaps(dbInterval)){
                return dayOff;
            }
        }
        return null;
    }

    public String reverse(String str){
        String s[] = str.split("-");
        String ans = "";
        for (int i = s.length - 1; i >= 0; i--) {
            ans += s[i] + "-";
        }
        return (ans.substring(0, ans.length() - 1));
    }
}
