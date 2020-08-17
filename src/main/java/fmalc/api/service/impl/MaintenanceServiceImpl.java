package fmalc.api.service.impl;

import fmalc.api.controller.NotificationController;
import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.DayOffEnum;
import fmalc.api.enums.NotificationTypeEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.repository.DayOffRepository;
import fmalc.api.repository.MaintenanceRepository;
import fmalc.api.repository.MaintenanceTypeRepository;
import fmalc.api.repository.VehicleRepository;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceRepository maintainanceRepository;
    @Autowired
    private UploaderService uploaderService;

    @Autowired
    NotificationController notificationController;

    @Autowired
    MaintenanceTypeRepository maintenanceTypeRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PlaceService placeService;

    @Autowired
    DriverService driverService;

    @Autowired
    DayOffService dayOffService;

    @Autowired
    DayOffRepository dayOffRepository;

    @Autowired
    NotificationService notificationService;

    private final static int DEFAULT_KM = 5000;
    private final static int DAYS = 24 * 60 * 60 * 1000;

    @Override
    public List<MaintainCheckDTO> checkMaintainForVehicle(int idVehicle) {
        List<Maintenance> maintenances = maintainanceRepository.findByVehicle(idVehicle);
        if (!maintenances.isEmpty()) {
            List<MaintainCheckDTO> maintainCheckDTOs = new MaintainCheckDTO().mapToListResponse(maintenances);

            Date date = new Date(System.currentTimeMillis());
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
//            List<Integer> id = maintainCheckDTOs.stream()
//                    .filter(x -> date.after(x.getPlannedMaintainDate()))
//                    .map(MaintainCheckDTO::getId)
//                    .collect(Collectors.toList());
//
//            maintainCheckDTOs.removeIf(x -> id.contains(x.getId()));

            if (!maintainCheckDTOs.isEmpty()) {
                return maintainCheckDTOs;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<MaintainCheckDTO> checkMaintainForDriver(int idDriver) {

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

    @Override
    public List<Maintenance> getListMaintenanceForDriver(int driverId) {

        List<Maintenance> maintenanceList = new ArrayList<>();
        maintenanceList = maintainanceRepository.findMaintenancesByDriverIdAndAndStatus(driverId, false);
        return maintenanceList;
    }

    @Override
    public Maintenance updateMaintainingComplete(int id, int km, MultipartFile file) throws IOException {
        Maintenance maintenance = maintainanceRepository.findByIdAndStatus(id, false);
        Date currentTime = new Date(System.currentTimeMillis());
        Vehicle vehicle = maintenance.getVehicle();
        if (maintenance != null) {
            Date actualTime = maintenance.getActualMaintainDate();
            if (currentTime.after(actualTime)) {
                if (maintenance.getKmOld() < km) {
                    maintenance.setKmOld(km);
                    String image = uploaderService.upload(file);
                    maintenance.setImageMaintain(image);
                    maintenance.setStatus(true);
                    //update km cho cả xe
//                    if()
                    vehicle.setKilometerRunning(km);
                    vehicleRepository.save(vehicle);
                }
            }


        }
        return maintainanceRepository.save(maintenance);
    }

    @Override
    public MaintainConfirmDTO updatePlannedTime(int id, int km) {
        Vehicle vehicle = vehicleRepository.findById(id).get();
        boolean res = false;
        java.util.Date date1 = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Maintenance maintenance = new Maintenance();
        if (vehicle.getId() != null) {
            List<Maintenance> maintenances = maintainanceRepository.findByVehicle(vehicle.getId());
            for (int i = 0; i < maintenances.size(); i++) {

                if (maintenances.get(i).getStatus() == false) {
                    maintenance = maintenances.get(i);
                    maintenances.remove(maintenances.get(i));
                    i = maintenances.size();
//                    maintainanceRepository.updateActualMaintainDate(maintenance.getId(),);
                }
            }

            maintenances.sort(Comparator.comparing(Maintenance::getActualMaintainDate));
            int kmRun = vehicle.getKilometerRunning();
            int kmOld = 0;
            if (maintenances.size() > 0) {
                kmOld = maintenances.get(maintenances.size() - 1).getKmOld();
            } else {
                if (maintenance != null) {
                    kmOld = maintenance.getKmOld();
                }

            }

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            Date dateS = new Date(date.getTime());

            if (maintenance.getId() != null) {
                if (maintenances.size() > 0) {
                    if (maintenance.getActualMaintainDate() != null) {
                        maintenance = new Maintenance();
                    } else {
                        int numDate = sdf.format(dateS).compareTo(sdf.format(maintenances.get(maintenances.size() - 1).getActualMaintainDate()));
                        long diff = dateS.getTime() - (maintenances.get(maintenances.size() - 1).getActualMaintainDate().getTime());
                        int diffDays = (int) (diff / DAYS);
                        if (diffDays == 0) {
                            diffDays = 1;
                        }
//                    int cal = 5000/((vehicle.getKilometerRunning()- kmOld)/diffDays);
                        int avg = 0;
                        if ((vehicle.getKilometerRunning() < kmOld)) {
                            avg = DEFAULT_KM / (((vehicle.getKilometerRunning() + kmOld) - kmOld) / diffDays);
                        } else {
                            avg = DEFAULT_KM / ((vehicle.getKilometerRunning() - kmOld) / diffDays);
                        }

                        int t = (kmOld + DEFAULT_KM) / DEFAULT_KM;
                        MaintenanceType maintenanceType = new MaintenanceType();
                        if (t % 2 == 0) {
                            maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 1");
                            maintenance.setMaintenanceType(maintenanceType);
                        } else if (t % 2 != 0) {
                            maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 2");
                            maintenance.setMaintenanceType(maintenanceType);
                        }


                        if (avg <= 7) {

                            date1 = checkScheduleForAVehicle(vehicle, 1);
                            if(checkDateMaintainForAVehicle(sdf.format(date1),vehicle.getId())){
                                maintenance.setActualMaintainDate(new Date(date1.getTime()));
                                maintenance.setPlannedMaintainDate(new Date(date1.getTime()));
                                List<Driver> drivers = findDriverForMaintain(vehicle.getWeight(), sdf.format(date1), vehicle.getId());
                                for (int i = 0; i < drivers.size(); i++) {
                                    if ((maintainanceRepository.findMaintenancesByDriverIdAndAndStatus(drivers.get(i).getId(), false)).size() <= 0) {
                                        maintenance.setDriver(drivers.get(i));
                                        i = drivers.size();
                                    }
                                }
                                maintenance.setVehicle(vehicle);
                                maintenance.setStatus(false);
                                if (maintainanceRepository.save(maintenance) != null) {

                                }
                            }


                        } else {
                            date1 = checkScheduleForAVehicle(vehicle, avg);
                            if(checkDateMaintainForAVehicle(sdf.format(date1),vehicle.getId())){
                                maintenance.setPlannedMaintainDate(new Date(date1.getTime()));
                                maintenance.setVehicle(vehicle);
                                maintenance.setStatus(false);
                                if (maintainanceRepository.save(maintenance) != null) {
                                    res = true;
                                }
                            }

                        }
                    }


                } else if (maintenances.size() <= 0) {
                    if (maintenance.getActualMaintainDate() == null) {
                        int numDate = sdf.format(dateS).compareTo(sdf.format(maintenance.getActualMaintainDate()));
                        long diff = dateS.getTime() - (maintenance.getActualMaintainDate().getTime());
                        int diffDays = (int) (diff / DAYS);
                        if (diffDays == 0) {
                            diffDays = 1;
                        }
                        int avg = 0;
                        if ((vehicle.getKilometerRunning() < kmOld)) {
                            avg = DEFAULT_KM / (((vehicle.getKilometerRunning() + kmOld) - kmOld) / diffDays);
                        } else {
                            avg = DEFAULT_KM / ((vehicle.getKilometerRunning() - kmOld) / diffDays);
                        }
                        int t = (kmOld + DEFAULT_KM) / DEFAULT_KM;
                        if (avg <= 7) {
                            date1 = checkScheduleForAVehicle(vehicle, 1);
                            if(checkDateMaintainForAVehicle(sdf.format(date1),vehicle.getId())){
                                maintenance.setActualMaintainDate(new Date(date1.getTime()));
                                maintenance.setPlannedMaintainDate(new Date(date1.getTime()));
                                List<Driver> drivers = findDriverForMaintain(vehicle.getWeight(), sdf.format(date1), vehicle.getId());
                                maintenance.setVehicle(vehicle);
                                for (int i = 0; i < drivers.size(); i++) {
                                    if ((maintainanceRepository.findMaintenancesByDriverIdAndAndStatus(drivers.get(i).getId(), false)).size() <= 0) {
                                        maintenance.setDriver(drivers.get(i));
                                        i = drivers.size();
                                    }
                                }
                                maintenance.setDriver(drivers.get(0));
                                maintenance.setStatus(false);
                                if (maintainanceRepository.save(maintenance) != null) {

                                }
                            }

                        } else {
                            date1 = checkScheduleForAVehicle(vehicle, avg);
//                        maintenance.setActualMaintainDate(new Date(date1.getTime()));
                            if(checkDateMaintainForAVehicle(sdf.format(date1),vehicle.getId())){
                                maintenance.setPlannedMaintainDate(new Date(date1.getTime()));
                                maintenance.setVehicle(vehicle);
                                maintenance.setStatus(false);
                                if (maintainanceRepository.save(maintenance) != null) {
                                    res = true;
                                }
                            }

                        }
                    } else {
                        maintenance = new Maintenance();
                    }
                } else if (maintenance != null && sdf.format(dateS).compareTo(sdf.format(maintenance.getActualMaintainDate())) <= 0) {
                    long diff = dateS.getTime() - (maintenance.getActualMaintainDate().getTime());
                    int diffDays = (int) (diff / DAYS);
                    if (diffDays == 0) {
                        diffDays = 1;
                    }
                    int avg = 0;
                    if ((vehicle.getKilometerRunning() < kmOld)) {
                        avg = DEFAULT_KM / (((vehicle.getKilometerRunning() + kmOld) - kmOld) / diffDays);
                    } else {
                        avg = DEFAULT_KM / ((vehicle.getKilometerRunning() - kmOld) / diffDays);
                    }
//                    int t = (kmOld + DEFAULT_KM) / DEFAULT_KM;
                    if (avg <= 7) {
                        date1 = checkScheduleForAVehicle(vehicle, 1);
                        if(checkDateMaintainForAVehicle(sdf.format(date1),vehicle.getId())){
                            maintenance.setActualMaintainDate(new Date(date1.getTime()));
                            maintenance.setPlannedMaintainDate(new Date(date1.getTime()));
                            List<Driver> drivers = findDriverForMaintain(vehicle.getWeight(), sdf.format(date1), vehicle.getId());
                            maintenance.setVehicle(vehicle);
                            for (int i = 0; i < drivers.size(); i++) {
                                if ((maintainanceRepository.findMaintenancesByDriverIdAndAndStatus(drivers.get(i).getId(), false)).size() <= 0) {
                                    maintenance.setDriver(drivers.get(i));
                                    i = drivers.size();
                                }
                            }
//                        maintenance.setDriver(drivers.get(0));
                            maintenance.setStatus(false);
                            if (maintainanceRepository.save(maintenance) != null) {

                            }
                        }

                    } else {
                        date1 = checkScheduleForAVehicle(vehicle, avg);
                        if(checkDateMaintainForAVehicle(sdf.format(date1),vehicle.getId())){
                            maintenance.setActualMaintainDate(null);
                            maintenance.setPlannedMaintainDate(new Date(date1.getTime()));
                            maintenance.setVehicle(vehicle);
                            maintenance.setStatus(false);
                            if (maintainanceRepository.save(maintenance) != null) {
//                            res = true;
                            }
                        }

                    }
                }
            } else {
                if (maintenances.size() > 0 && sdf.format(dateS).compareTo(sdf.format(maintenances.get(maintenances.size() - 1).getActualMaintainDate())) >= 0) {
                    int numDate = sdf.format(dateS).compareTo(sdf.format(maintenances.get(maintenances.size() - 1).getActualMaintainDate()));
                    long diff = dateS.getTime() - (maintenances.get(maintenances.size() - 1).getActualMaintainDate().getTime());
                    int diffDays = (int) (diff / DAYS);
                    if (diffDays == 0) {
                        diffDays = 1;
                    }
                    int avg = 0;
                    if ((vehicle.getKilometerRunning() < kmOld)) {
                        avg = DEFAULT_KM / (((vehicle.getKilometerRunning() + kmOld) - kmOld) / diffDays);
                    } else {
                        avg = DEFAULT_KM / ((vehicle.getKilometerRunning() - kmOld) / diffDays);
                    }
                    int t = (kmOld + DEFAULT_KM) / DEFAULT_KM;
                    MaintenanceType maintenanceType = new MaintenanceType();
                    if (t % 2 == 0) {
                        maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 1");
                        maintenance.setMaintenanceType(maintenanceType);
                    } else if (t % 2 != 0) {
                        maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 2");
                        maintenance.setMaintenanceType(maintenanceType);
                    }


                    if (avg <= 7) {
                        date1 = checkScheduleForAVehicle(vehicle, 1);
                        maintenance.setActualMaintainDate(new Date(date1.getTime()));
                        maintenance.setPlannedMaintainDate(new Date(date1.getTime()));
                        List<Driver> drivers = findDriverForMaintain(vehicle.getWeight(), sdf.format(date1), vehicle.getId());
                        for (int i = 0; i < drivers.size(); i++) {
                            if ((maintainanceRepository.findMaintenancesByDriverIdAndAndStatus(drivers.get(i).getId(), false)).size() <= 0) {
                                maintenance.setDriver(drivers.get(i));
                                i = drivers.size();
                            }
                        }
                        maintenance.setVehicle(vehicle);
                        maintenance.setStatus(false);
                        if (maintainanceRepository.save(maintenance) != null) {

                        }
                    } else {
//                        Calendar c = Calendar.getInstance();
//                        try {
//                            c.setTime(sdf.parse(sdf.format(date1)));
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//
//                        }
//                        c.add(Calendar.DAY_OF_MONTH, avg);
                        date1 = checkScheduleForAVehicle(vehicle, avg);
                        maintenance.setPlannedMaintainDate(new Date(date1.getTime()));
                        maintenance.setVehicle(vehicle);
                        maintenance.setStatus(false);
                        if (maintainanceRepository.save(maintenance) != null) {
                            res = true;
                        }
                    }
                }
            }


        }
        MaintainReponseDTO maintainReponseDTO = new MaintainReponseDTO();
        DriverForDetailDTO driverForDetailDTO = new DriverForDetailDTO();
        maintainReponseDTO = maintainReponseDTO.convertSchedule(maintenance);
        List<DriverForDetailDTO> driverForDetailDTOS = new ArrayList<>();
        driverForDetailDTOS = driverForDetailDTO.mapToListResponse(findDriverForMaintain(vehicle.getWeight(), sdf.format(date1), vehicle.getId()));
        MaintainConfirmDTO maintainConfirmDTO = new MaintainConfirmDTO();
        maintainConfirmDTO.setDriverForDetailDTOS(driverForDetailDTOS);
        maintainConfirmDTO.setMaintainReponseDTO(maintainReponseDTO);
        return maintainConfirmDTO;
    }

    private List<Driver> findDriverForMaintain(double weight, String date, int idVehicle) {
        List<Driver> drivers = driverService.findDriverByLicense(weight);
        List<Driver> result = new ArrayList<>();
        boolean flag = true;
        if (drivers.size() > 0) {
            for (int i = 0; i < drivers.size(); i++) {
                Driver driver = drivers.get(i);
                flag = checkDateMaintain(date, checkMaintainForDriver(driver.getId()), idVehicle);
                if (flag) {
                    flag = checkSchedule(driver.getId(), date);
                    if (flag) {
                        flag = checkDayOff(date, driver.getId());
                    }
                }
                if (flag) {
                    result.add(driver);
                }
            }
        }
        result.sort(Comparator.comparing(Driver::getWorkingHour));
        return drivers;
    }

    private Boolean checkDateConfirm(Driver driver, String date, int idVehicle) {
        boolean flag = true;
        List<MaintainCheckDTO> list = checkMaintainForDriver(driver.getId());
        flag = checkDateMaintain(date, list, idVehicle);
        if (flag) {
            flag = checkSchedule(driver.getId(), date);
            if (flag) {
                flag = checkDayOff(date, driver.getId());
            }
        }
        if (flag) {
            return true;
        }
        return false;
    }

    private boolean checkDayOff(String dates, int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<DayOff> dayOffs = new ArrayList<>();
        boolean flag = false;
        dayOffs = dayOffRepository.checkDayOffOfDriver(id, DayOffEnum.APPROVED.getValue());
        if (dayOffs.size() > 0) {
            for (int j = 0; j < dayOffs.size(); j++) {
                String dateOff = sdf.format(dayOffs.get(j).getStartDate());

                if (dateOff.compareTo(dates) >= 1) {
                    flag = true;
                } else if (dateOff.compareTo(dates) <= -1) {
                    String dateEnd = sdf.format(dayOffs.get(j).getEndDate());
                    if (dateEnd.compareTo(dates) >= 1) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                }

                if (flag == false) {
                    j = dayOffs.size();
                } else {

                }
            }


//                String dateConsignment =
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


    private boolean checkDateMaintain(String dateS, List<MaintainCheckDTO> maintainCheckDTO, int idVehicle) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = false;
        if (maintainCheckDTO.size() > 0) {
            for (int i = 0; i < maintainCheckDTO.size(); i++) {
                String dateMaintain = sdf.format(maintainCheckDTO.get(i).getPlannedMaintainDate());
                if (dateS.compareTo(dateMaintain) > 0) {
                    flag = true;
                } else {
                    Maintenance maintenance = maintainanceRepository.findById(maintainCheckDTO.get(i).getId()).get();
                    if (maintenance.getVehicle().getId() == idVehicle) {
                        flag = true;
                    } else {
                        flag = false;
                    }

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

    private boolean checkSchedule(int id, String dateC) {
//        boolean flag = true;
        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        List<Driver> k = new ArrayList<>();
        java.util.Date result = new java.util.Date();
        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
        scheduleForConsignmentDTOS = checkScheduleForDriver(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                    if (dateC.compareTo(dateRecei) < 0) {
                        if (dateC.compareTo(sdf.format(result)) > 0) {
                            flag = true;
                        } else {
                            flag = false;
                            j = scheduleForConsignmentDTOS.size();
                        }
                    } else if (dateC.compareTo(dateRecei) > 0 && dateC.compareTo(dateRecei) > 0) {
                        if (dateC.compareTo(dateDeli) > 0 && (dateC.compareTo(dateDeli) >= dateDeli.compareTo(dateRecei))) {
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

    private java.util.Date checkScheduleForAVehicle(Vehicle vehicle, int avg) {
        List<Schedule> schedules = scheduleService.checkVehicleInScheduled(vehicle.getId());
        java.util.Date result = new java.util.Date();
        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
        int i = avg;
        boolean flag = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        if (schedules != null && schedules.size() > 0) {
            scheduleForConsignmentDTOS = scheduleForLocationDTO.mapToListResponse(schedules);
            do {
                try {
                    result = sdf.parse(sdf.format(result));
                    c.setTime(sdf.parse(sdf.format(result)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.add(Calendar.DAY_OF_MONTH, i);
                String dateC = sdf.format(c.getTime());
                for (int j = 0; j < scheduleForConsignmentDTOS.size(); j++) {
                    scheduleForLocationDTO = scheduleForConsignmentDTOS.get(j);
                    List<PlaceResponeDTO> listScheduleDeli =
                            placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                    PlaceResponeDTO placeScheduleRecei =
                            placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());
                    PlaceResponeDTO placeScheduleDeli =
                            placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), listScheduleDeli.size(), TypeLocationEnum.RECEIVED_PLACE.getValue());

                    String dateRecei = sdf.format(placeScheduleRecei.getPlannedTime());
                    String dateDeli = sdf.format(placeScheduleDeli.getPlannedTime());
                    if (dateC.compareTo(dateRecei) < 0) {
                        if (dateC.compareTo(sdf.format(result)) > 0) {
                            flag = true;
                        } else {
                            j = scheduleForConsignmentDTOS.size();
                            i++;
                        }
                    } else if (dateC.compareTo(dateRecei) > 0 && dateC.compareTo(dateRecei) > 0) {
                        if (dateC.compareTo(dateDeli) > 0) {
                            flag = true;
                        } else {
//                            flag =false;
                            j = scheduleForConsignmentDTOS.size();
                            i++;
                        }
                    } else {
                        j = scheduleForConsignmentDTOS.size();
                        i++;
                    }
                    if (flag && j == scheduleForConsignmentDTOS.size()) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }
                if(!flag){
                   if(checkDateMaintainForAVehicle(dateC,vehicle.getId())){
                       flag = false;
                   }
                }

            } while (flag);
        }


        return c.getTime();
    }

    @Override
    public List<MaintainReponseDTO> getListMaintainByVehicle(int idVehicle) {
        List<Maintenance> maintenances = maintainanceRepository.findByVehicle(idVehicle);
        MaintainReponseDTO maintainReponseDTO = new MaintainReponseDTO();
        List<MaintainReponseDTO> maintainReponseDTOS = maintainReponseDTO.mapToListResponse(maintenances);
        return maintainReponseDTOS;
    }

    @Override
    public void calculateMaintenanceForVehicle(int idVehicle) {
        List<Maintenance> maintenances = maintainanceRepository.findTop2ByVehicle_IdOrderByIdDesc(idVehicle);
        LocalDate today = LocalDate.now();
        Maintenance maintenance = maintenances.get(0);
        if (maintenance.getActualMaintainDate() != null && !maintenance.getStatus()) {
            return;
        }
        boolean isUpdate = true;
        if (maintenances.size() == 1 || maintenance.getActualMaintainDate() != null) {
            isUpdate = false;
        } else {
            maintenance = maintenances.get(1);
        }

        Vehicle vehicle = maintenance.getVehicle();
        //Increase kmRunning hardcode
        vehicleRepository.updateKmRunning(idVehicle, vehicle.getKilometerRunning() + 1000);
        vehicle.setKilometerRunning(vehicle.getKilometerRunning() + 1000);

        long daysBetween = Duration.between(maintenance.getActualMaintainDate().toLocalDate().atStartOfDay(), today.atStartOfDay()).toDays();
        if (daysBetween == 0) daysBetween = 1;
        double averageKmOnDays = (vehicle.getKilometerRunning() - maintenance.getKmOld()) / (daysBetween);
        long dayNextMaintain = (long) Math.ceil((maintenance.getKmOld() + 5000 - vehicle.getKilometerRunning()) / averageKmOnDays);
        Date next = Date.valueOf(today.plusDays(dayNextMaintain));

        if (isUpdate) {
            maintainanceRepository.updatePlannedMaintainDate(maintenances.get(0).getId(), next);
            if (Duration.between(today.atStartOfDay(), next.toLocalDate().atStartOfDay()).toDays() <= 7) {
                maintainanceRepository.updateActualMaintainDate(maintenances.get(0).getId(), next);
                //Assign Driver
                // Notify
                NotificationRequestDTO requestDTO = new NotificationRequestDTO();
                requestDTO.setContent("Lịch bảo trì cho xe " + vehicle.getLicensePlates());
                //hardcode driver
                requestDTO.setDriver_id(1);
                requestDTO.setVehicle_id(idVehicle);
                requestDTO.setStatus(true);
                requestDTO.setType(4);
                notificationController.createNotification(requestDTO);
            }
            return;
        }

        Maintenance addMaintenance = new Maintenance();
        addMaintenance.setPlannedMaintainDate(next);
        addMaintenance.setKmOld(maintenances.get(0).getKmOld() + 5000);
        addMaintenance.setVehicle(vehicle);
        MaintenanceType maintenanceType;
        if ("Loại 1".equals(maintenance.getMaintenanceType().getMaintenanceTypeName())) {
            maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 2");
        } else {
            maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 1");
        }
        addMaintenance.setMaintenanceType(maintenanceType);
        addMaintenance.setStatus(false);
        maintainanceRepository.save(addMaintenance);
        if (Duration.between(today.atStartOfDay(), next.toLocalDate().atStartOfDay()).toDays() <= 7) {
            maintainanceRepository.updateActualMaintainDate(maintenances.get(0).getId(), next);
            //Assign Driver
            // Notify
            NotificationRequestDTO requestDTO = new NotificationRequestDTO();
            requestDTO.setContent("Lịch bảo trì cho xe " + vehicle.getLicensePlates());
            //hardcode driver
            requestDTO.setDriver_id(1);
            requestDTO.setVehicle_id(idVehicle);
            requestDTO.setStatus(true);
            requestDTO.setType(4);
            notificationController.createNotification(requestDTO);
        }
    }

    @Override
    public List<Maintenance> getMaintenanceListForConfirm() {
        List<Maintenance> maintenances = maintainanceRepository.findAllByActualMaintainDateIsNotNullAndStatusIsFalseOrderByActualMaintainDateDesc();
        for(int i=0; i< maintenances.size();i++){
            if(maintenances.get(i).getKmOld()==null){
                List<Maintenance> checks = maintainanceRepository.findByVehicle(maintenances.get(i).getVehicle().getId());
                if(checks.size()>0){
                    int max =0;
                    for(int j=0; j< checks.size();j++){
                        if(checks.get(j).getKmOld()!=null){
                            max = checks.get(j).getKmOld();
                        }
                    }
//                    checks.sort(Comparator.comparing(Maintenance::getKmOld));
                    maintenances.get(i).setKmOld(max);
                }else{
                    maintenances.get(i).setKmOld(0);
                }
            }
        }
        return maintenances;
    }

    @Override
    public List<Maintenance> getMaintenance() {
        return maintainanceRepository.findAllByActualMaintainDateIsNotNullOrderByActualMaintainDateDesc();
    }

    @Override
    public void createFirstMaintain(Vehicle vehicle) {
        LocalDate today = LocalDate.now();
        Maintenance addMaintenance = new Maintenance();
        addMaintenance.setPlannedMaintainDate(Date.valueOf(today));
        addMaintenance.setActualMaintainDate(Date.valueOf(today));
        addMaintenance.setKmOld(vehicle.getKilometerRunning());
        addMaintenance.setVehicle(vehicle);
        addMaintenance.setStatus(true);
        MaintenanceType maintenanceType = new MaintenanceType();
        if (vehicle.getKilometerRunning() <= 5000) {
            maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 3");
            addMaintenance.setMaintenanceType(maintenanceType);
        } else {
            maintenanceType = maintenanceTypeRepository.findByMaintenanceTypeName("Loại 1");
            addMaintenance.setMaintenanceType(maintenanceType);
        }

        Maintenance maintenance = maintainanceRepository.save(addMaintenance);
    }

    @Override
    public List<java.util.Date> dateConfirm(int idVehicle, int idDriver, java.util.Date date) {

        List<java.util.Date> dates = new ArrayList<>();
        Vehicle vehicle = vehicleRepository.findByIdVehicle(idVehicle);
        Driver driver = driverService.findById(idDriver);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            boolean result = checkDateConfirm(driver, sdf.format(date), idVehicle);
        if (result) {
            result = checkScheduleForVeToConfirm(vehicle, sdf.format(date));
            if (result) {
                result = checkDateMaintainForAVehicle(sdf.format(date), idVehicle);
                if (result) {
//                    result=true;
                    Maintenance maintenance = maintainanceRepository.findByIdVehicleAndStatus(idVehicle, false);
                    maintenance.setActualMaintainDate(new Date(date.getTime()));
                    maintenance = maintainanceRepository.save(maintenance);
                    dates.add(date);
                    try {
                        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
                        notificationRequestDTO.setVehicle_id(maintenance.getVehicle().getId());
                        notificationRequestDTO.setDriver_id(driver.getId());
                        notificationRequestDTO.setStatus(false);
                        notificationRequestDTO.setType(NotificationTypeEnum.MAINTAIN_SCHEDULE.getValue());
                        notificationRequestDTO.setContent("Bạn có lịch đi bảo trì xe vào ngày " + maintenance.getActualMaintainDate());
                        notificationService.createNotification(notificationRequestDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        if (!result) {
            java.util.Date date1 = new java.util.Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            int numDayCheck = 7;
            for (int i = 1; i <= numDayCheck; i++) {
//                c.add(Calendar.DAY_OF_MONTH, i);
                java.util.Date tmp = checkScheduleForAVehicle(vehicle, i);
                result = checkScheduleForVeToConfirm(vehicle, sdf.format(tmp));
                if (result) {
                    result = checkDateMaintainForAVehicle(sdf.format(tmp), idVehicle);
                    if (result) {
                        if (checkDateConfirm(driver, (sdf.format(tmp)), idVehicle)) {
                            if (!dates.contains(tmp)) {
                                dates.add(tmp);
                            }
                        }
                        if (i + 1 == numDayCheck && dates.size() <= 0) {
                            numDayCheck += 3;
                        }
                    }
                }
            }

        }
            return dates;
    }

    private boolean checkScheduleForVeToConfirm(Vehicle vehicle, String date) {
        List<Schedule> schedules = scheduleService.checkVehicleInScheduled(vehicle.getId());
        java.util.Date result = new java.util.Date();
        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        List<ScheduleForConsignmentDTO> scheduleForConsignmentDTOS = new ArrayList<>();
//        int i = avg;
        boolean flag = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        if (schedules != null && schedules.size() > 0) {
            scheduleForConsignmentDTOS = scheduleForLocationDTO.mapToListResponse(schedules);
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
                    if (date.compareTo(dateRecei) < 0) {
                        if (date.compareTo(sdf.format(result)) > 0) {
                            flag = true;
                        } else {
                            flag = false;
                            j = scheduleForConsignmentDTOS.size();
                        }
                    } else if (date.compareTo(dateRecei) > 0 && date.compareTo(dateRecei) > 0) {
                        if (date.compareTo(dateDeli) > 0 && (date.compareTo(dateDeli) >= dateDeli.compareTo(dateRecei))) {
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
        }


        return flag;
    }


    private boolean     checkDateMaintainForAVehicle(String dateS, int idVehicle) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = false;
//        Date date = new Date();
        List<Maintenance> maintenances = maintainanceRepository.findByVehicle(idVehicle);
        if (maintenances.size() > 0) {
            for (int i = 0; i < maintenances.size(); i++) {
//                flag = false;
                if (maintenances.get(i).getStatus() == true) {
//                    flag = true;
                } else {
                    String dateMaintain;
                    if(maintenances.get(i).getActualMaintainDate()!=null){
                        dateMaintain = sdf.format(maintenances.get(i).getActualMaintainDate());
                    }else{
                        dateMaintain = sdf.format(maintenances.get(i).getPlannedMaintainDate());
                    }

                    if (dateS.compareTo(dateMaintain) > 0 || dateS.compareTo(dateMaintain) < 0) {
                        flag = true;
                    } else {

                    }
                    if (!flag) {
//                        flag = false;
                        i = maintenances.size();
                    }
                }
            }
        } else {
            flag = true;
        }

        return flag;
    }
    // Start date : lúc bấm nút kết thúc  -- THANHDC
//    @Override
//    public Integer countMaintenanceScheduleNumberInADayOfDriver(Integer driverId, Timestamp startDate, Timestamp endDate) {
//
//        return maintainanceRepository.countMaintenanceScheduleNumberInADayOfDriver(driverId, startDate, endDate);
//    }

}
