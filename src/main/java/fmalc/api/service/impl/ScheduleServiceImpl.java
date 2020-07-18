
package fmalc.api.service.impl;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    MaintainanceService maintainanceService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PlaceService placeService;

    @Autowired
    DayOffService dayOffService;


    private static int priorityPlace = 1;

    @Override
    public List<ScheduleForLocationDTO> getScheduleByConsignmentId(int id) {
        //-----------------------------------------------------------------------
        ScheduleForLocationDTO scheduleForLocationDTO = new ScheduleForLocationDTO();
        List<Schedule> schedules = scheduleRepository.findByConsignment_Id(id);
        List<ScheduleForLocationDTO> scheduleForLocationDTOs = new ArrayList<>();
        for (int i = 0; i < schedules.size(); i++) {
            scheduleForLocationDTO = scheduleForLocationDTO.convertSchedule(schedules.get(i));
            scheduleForLocationDTO.setVehicle_id(schedules.get(i).getVehicle().getId());
            scheduleForLocationDTO.setDriver_id(schedules.get(i).getDriver().getId());
            scheduleForLocationDTOs.add(scheduleForLocationDTO);
        }
        return scheduleForLocationDTOs;
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {

        Schedule schedule1 = new Schedule();
        schedule1.setNote(schedule.getNote());
        schedule1.setImageConsignment(schedule.getImageConsignment());
        schedule1.setConsignment(schedule.getConsignment());
        schedule1.setDriver(schedule.getDriver());
        schedule1.setVehicle(schedule.getVehicle());
        schedule1.setIsApprove(schedule.getIsApprove());
        schedule1.setId(null);
        schedule1 = scheduleRepository.save(schedule1);


        return schedule1;
    }

    /*
     * Lấy danh sách xe có trạng thái hiện tại là Available
     * Kiểm tra danh sách xe này có lịch bảo trì vs lịch chạy trong tương lai không
     * Trả về danh sách xe không có việc bận từ hiện tại tới tương lai]
     *
     */


    @Override
    public List<Vehicle> findVehicleForSchedule(Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO) {
        boolean flag = true;
//        List<Vehicle> vehiclesAvailable = vehicleService.findByStatus(VehicleStatusEnum.AVAILABLE.getValue(), consignment.getWeight());
//

        List<VehicleConsignmentDTO> vehicleConsignmentDTOS = consignmentRequestDTO.getVehicles();
        List<Vehicle> vehicles = new ArrayList<>();
//        List<Vehicle> vehiclesScheduled;
        List<Vehicle> result = new ArrayList<>();
        for (int i = 0; i < vehicleConsignmentDTOS.size(); i++) {
            int size = Integer.parseInt(vehicleConsignmentDTOS.get(i).getQuantity());
            if (size > 0) {
                double weight = Double.parseDouble(vehicleConsignmentDTOS.get(i).getWeight());
//            vehiclesScheduled  = vehicleService.findByStatus(VehicleStatusEnum.SCHEDULED.getValue(), consignment.getWeight());
                vehicles = vehicleService.findByWeight(weight);
//        if(vehicles.contains())
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                if (vehicles.size() > 0) {
                    vehicles = checkMaintainForVehicle(vehicles, consignment);
                    vehicles = checkScheduledForVehicle(vehicles, consignment);
                    if (vehicles.size() > 0) {
                        if (vehicles.size() >= size) {
                            result.addAll(vehicles);
                        } else {
                            List<Vehicle> vehicleBigger = vehicleService.findByWeightBigger(weight);
                            if (vehicleBigger.size() > 0) {
                                vehicleBigger = checkMaintainForVehicle(vehicleBigger, consignment);
                                vehicleBigger = checkScheduledForVehicle(vehicleBigger, consignment);
                                if (vehicleBigger.size() > 0 && vehicleBigger.size() >= (size - vehicles.size())) {
                                    vehicles.addAll(vehicleBigger);
                                    result.addAll(vehicles);
                                } else {
                                    vehicles.addAll(vehicleBigger);
                                    result.addAll(vehicles);
                                }
                            } else {
                                //vehicleBigger size = 0
                            }
                        }
                    } else {
                        //vehicles size =0
                        if (result.size() > 0) {
                            result = new ArrayList<>();
                        }
                        List<Vehicle> vehicleBigger = vehicleService.findByWeightBigger(weight);
                        if (vehicleBigger.size() > 0) {
                            vehicleBigger = checkMaintainForVehicle(vehicleBigger, consignment);
                            vehicleBigger = checkScheduledForVehicle(vehicleBigger, consignment);
                            if (vehicleBigger.size() > 0 && vehicleBigger.size() >= (size - vehicles.size())) {
                                vehicles.addAll(vehicleBigger);
                                result.addAll(vehicles);
                            } else {
                                vehicles.addAll(vehicleBigger);
                                result.addAll(vehicles);
                            }
                        } else {
                            //vehicleBigger size = 0
                        }
                    }

                } else {
                    if (result.size() > 0) {
                        result = new ArrayList<>();
                    }
                    List<Vehicle> vehicleBigger = vehicleService.findByWeightBigger(weight);
                    if (vehicleBigger.size() > 0) {
                        vehicleBigger = checkMaintainForVehicle(vehicleBigger, consignment);
                            vehicleBigger = checkScheduledForVehicle(vehicleBigger, consignment);
                        if (vehicleBigger.size() > 0 && vehicleBigger.size() >= (size - vehicles.size())) {
                            vehicles.addAll(vehicleBigger);
                            result.addAll(vehicles);
                        } else {
                            vehicles.addAll(vehicleBigger);
                            result.addAll(vehicles);
                        }
                    } else {
                        //vehicleBigger size = 0
                    }
                }
            }

        }
        return result;
    }

    @Override
    public List<Driver> findDriverForSchedule(double weight, Consignment consignment) {
//        VehicleReponseDTO vehicleReponseDTO = new VehicleReponseDTO();
//        vehicleReponseDTO = vehicleReponseDTO.convertVehicle(vehicle);
        List<Driver> drivers = driverService.findDriverByLicense(weight);
        if (drivers.size() > 0) {
            drivers = checkMaintainForDriver(drivers, consignment);
            drivers = checkScheduledForDriver(drivers, consignment);
            drivers = dayOffService.checkDayOffODriver(drivers, consignment);
        }
        return drivers;
    }

    /*
     * Kiểm tra xe có lịch chạy từ ngày hiện tại đến tương lai
     * Trả về danh sách chạy trong tương lai của xe
     * */
    @Override
    public List<ScheduleForConsignmentDTO> checkScheduleForVehicle(int idVehicle) {
        List<ScheduleForConsignmentDTO> scheduleForLocationDTOS = new ArrayList<>();
        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        List<ScheduleForConsignmentDTO> result = new ArrayList<>();
        boolean flag;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        List<Schedule> Schedules = scheduleRepository.checkVehicleInScheduled(idVehicle);
        if (Schedules.size() > 0) {
            scheduleForLocationDTOS = scheduleForLocationDTO.mapToListResponse(Schedules);
            if (scheduleForLocationDTOS.size() > 0) {
                for (int i = 0; i < scheduleForLocationDTOS.size(); i++) {
                    flag = true;
                    PlaceResponeDTO deliveryDetail = placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTOS.get(i).getConsignment().getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
                    if (deliveryDetail.getPlannedTime() != null) {
                        String datePlace = sdf.format(deliveryDetail.getPlannedTime());
                        String dateNow = sdf.format(new Date());
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

    @Override
    public List<ScheduleForConsignmentDTO> checkScheduleForDriver(int idDriver) {
//        boolean flag =true;
        List<ScheduleForConsignmentDTO> scheduleForLocationDTOS = new ArrayList<>();
        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        List<ScheduleForConsignmentDTO> result = new ArrayList<>();
        boolean flag = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Schedule> Schedules = scheduleRepository.checkDriverInScheduled(idDriver);
        if (Schedules.size() > 0) {

            scheduleForLocationDTOS = scheduleForLocationDTO.mapToListResponse(Schedules);
            if (scheduleForLocationDTOS.size() > 0) {
                for (int i = 0; i < scheduleForLocationDTOS.size(); i++) {
                    flag = true;
                    PlaceResponeDTO deliveryDetail = new PlaceResponeDTO();
                    deliveryDetail = placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTOS.get(i).getConsignment().getId(), priorityPlace, TypeLocationEnum.RECEIVED_PLACE.getValue());
                    if (deliveryDetail.getPlannedTime() != null) {
                        String datePlace = sdf.format(deliveryDetail.getPlannedTime());
                        String dateNow = sdf.format(new Date());
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
//                for (int j = 0; j < id.size(); j++) {
//                    flag = true;
//                    for (int i = 0; i < scheduleForLocationDTOS.size(); i++) {
//                        if (flag) {
//                            if (scheduleForLocationDTOS.get(i).getId() == id.get(j)) {
//                                scheduleForLocationDTOS.remove(scheduleForLocationDTOS.get(i));
//                                flag = false;
//                            }
//                        }
//                    }
//
//                }

            }


        }
        return scheduleForLocationDTOS;
    }

    @Override
    public List<ScheduleForLocationDTO> getScheduleToCheck() {
        ScheduleForLocationDTO scheduleForLocationDTO = new ScheduleForLocationDTO();
        List<Schedule> schedules = scheduleRepository.findAll();

        List<ScheduleForLocationDTO> scheduleForLocationDTOS = scheduleForLocationDTO.mapToListResponse(schedules);
        for (int i = 0; i < schedules.size(); i++) {
            scheduleForLocationDTOS.get(i).setVehicle_id(schedules.get(i).getVehicle().getId());
            scheduleForLocationDTOS.get(i).setDriver_id(schedules.get(i).getDriver().getId());
        }
        return scheduleForLocationDTOS;
    }


    //check lịch bảo trì xe có trùng vơi lịch đi giao hàng không
    private List<Vehicle> checkMaintainForVehicle(List<Vehicle> vehicles, Consignment consignment) {

        boolean flag = true;
        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();
        List<Vehicle> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        for (int i = 0; i < vehicles.size(); i++) {
            flag = true;
            maintainCheckDTO = new MaintainCheckDTO();
//            double weight = consignment.getWeight();
//            VehicleForDetailDTO vehicle = vehicleService.findVehicleById(vehicles.get(i).getId());
            //check xe co lich bao tri trong tuong lai
            maintainCheckDTO = maintainanceService.checkMaintainForVehicle(vehicles.get(i).getId());
            if (maintainCheckDTO.getId() != null) {


                //list place receive of a consignment
                flag = checkDateMaintain(consignment, maintainCheckDTO, flag);

            }
            if (flag) {
                result.add(vehicles.get(i));
            }
        }

        return result;
    }

    //check lịch đi bảo trì của tài xế  có trùng vơi lịch đi giao hàng không
    private List<Driver> checkMaintainForDriver(List<Driver> drivers, Consignment consignment) {

        boolean flag = true;
        MaintainCheckDTO maintainCheckDTO = new MaintainCheckDTO();
        List<Driver> result = new ArrayList<>();

        for (int i = 0; i < drivers.size(); i++) {
            flag = true;
            //            VehicleForDetailDTO vehicle = vehicleService.findVehicleById(drivers.get(i).getId());
            //check xe co lich bao tri trong tuong lai
            maintainCheckDTO = maintainanceService.checkMaintaiinForDriver(drivers.get(i).getId());
            if (maintainCheckDTO.getId() != null) {

                flag = checkDateMaintain(consignment, maintainCheckDTO, flag);

            }

            if (flag) {
                result.add(drivers.get(i));
            }
        }

        return result;
    }

    private boolean checkDateMaintain(Consignment consignment, MaintainCheckDTO maintainCheckDTO, boolean flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        PlaceResponeDTO placeSchedulePriorityRecei =
                placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), 1, TypeLocationEnum.DELIVERED_PLACE.getValue());

        //list place delivery of a consignment
        List<PlaceResponeDTO> placeConsgimentsPriorityDeli =
                placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());

        //receive and priority =1
        if (placeSchedulePriorityRecei != null) {
            String dateReceiConsignment = sdf.format(placeSchedulePriorityRecei.getPlannedTime());
            String dateMaintain = sdf.format(maintainCheckDTO.getPlannedMaintainDate());
            if (dateReceiConsignment.compareTo(dateMaintain) <= 1) {
                PlaceResponeDTO placeSchedulePriorityDeli =
                        placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), placeConsgimentsPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());
                if (placeConsgimentsPriorityDeli != null) {
                    String dateConsignemntDeli = sdf.format(placeSchedulePriorityDeli.getPlannedTime());
                    if (dateConsignemntDeli.compareTo(dateMaintain) <= 1) {
                        flag = false;
                    }
                }

            } else if (dateReceiConsignment.compareTo(dateMaintain) >= 1) {
                flag = false;
            }
        }
        return flag;
    }

    // lấy những xe có lich chạy để kiểm tra có trùng lịch sắp tạo không
    private List<Vehicle> getVehiclesNoSchedule(List<Vehicle> vehicles, Consignment consignment) {

        boolean flag = true;
        List<ScheduleForConsignmentDTO> scheduleForLocationDTOS = new ArrayList<>();
        List<Vehicle> result = new ArrayList<>();


        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        for (int i = 0; i < vehicles.size(); i++) {
            flag = true;

            scheduleForLocationDTOS = checkScheduleForVehicle(vehicles.get(i).getId());
            if (scheduleForLocationDTOS.size() > 0) {
                for (int j = 0; j < scheduleForLocationDTOS.size(); j++) {

                    scheduleForLocationDTO = scheduleForLocationDTOS.get(j);
                    flag = checkDateConsignmentAndSchedule(scheduleForLocationDTO, consignment, flag);

//                    //list place delivery of a schedule


                    if (flag) {
                        j = scheduleForLocationDTOS.size();
                    }
                }
                if (!flag) {
                    result.add(vehicles.get(i));

                }
            } else {
                result.add(vehicles.get(i));
            }

        }
        return result;
    }

    private List<Vehicle> checkScheduledForVehicle(List<Vehicle> vehicles, Consignment consignment) {

        boolean flag = true;
        List<ScheduleForConsignmentDTO> scheduleForLocationDTOS = new ArrayList<>();
        List<Vehicle> result = new ArrayList<>();


        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        for (int i = 0; i < vehicles.size(); i++) {
            flag = true;
//            double weight = consignment.getWeight();
            //check xe co lich bao tri trong tuong lai
            scheduleForLocationDTOS = checkScheduleForVehicle(vehicles.get(i).getId());
            if (scheduleForLocationDTOS.size() > 0) {
                for (int j = 0; j < scheduleForLocationDTOS.size(); j++) {

                    scheduleForLocationDTO = scheduleForLocationDTOS.get(j);
                    flag = checkDateConsignmentAndSchedule(scheduleForLocationDTO, consignment, flag);

                    if (flag) {
                        j = scheduleForLocationDTOS.size();
                    }
                }
                if (!flag) {
                    result.add(vehicles.get(i));

                }
            } else {
                result.add(vehicles.get(i));
            }

        }
        return result;
    }

    private boolean checkDateConsignmentAndSchedule(ScheduleForConsignmentDTO scheduleForLocationDTO, Consignment consignment, boolean flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        flag = true;
        List<PlaceResponeDTO> listScheduleDeli =
                placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());
//
//                    //list place delivery of a consignment
        List<PlaceResponeDTO> listConsginmentDeli =
                placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());

        // Consignemnt
        PlaceResponeDTO placeConsignmentRecei =
                placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());

        PlaceResponeDTO placeConsignmentDeli =
                placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), listConsginmentDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

        // Schedule
        // lấy thời gian consignment  lấy hàng có độ ưu tiên 1
        PlaceResponeDTO placeScheduleRecei =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());
        // lấy thằng thời gian giao hàng sau cùng
        PlaceResponeDTO placeScheduleDeli =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), listScheduleDeli.size(), TypeLocationEnum.RECEIVED_PLACE.getValue());

        if (placeConsignmentRecei.getAddress() != null && placeScheduleRecei.getAddress() != null) {
            String dateReceiConsignment = sdf.format(placeConsignmentRecei.getPlannedTime());
            String dateScheduleRecei = sdf.format(placeScheduleRecei.getPlannedTime());
            if (dateReceiConsignment.compareTo(dateScheduleRecei) >= 1) {
//                        PlaceResponeDTO placeSchedulePriorityDeli =
//                                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), placeSchedulesPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());


                if (placeScheduleDeli.getAddress() != null) {
                    String dateScheduleDe = sdf.format(placeScheduleDeli.getPlannedTime());
                    if (dateReceiConsignment.compareTo(dateScheduleDe) >= 1) {
                        flag = false;
//                                    l = placeConsignmentPriority.size();// out for check time
                        //xe pass 1 schedule;
                    }
                }

            } else if (dateReceiConsignment.compareTo(dateScheduleRecei) <= -1) {
//                        PlaceResponeDTO placeConsignmentPriorityDeli =
//                                placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), placeConsgimentsPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());
                if (placeConsignmentDeli != null) {
                    String dateConsignmentDeli = sdf.format(placeConsignmentDeli.getPlannedTime());
                    if (dateConsignmentDeli.compareTo(dateScheduleRecei) <= -1) {
                        flag = false;
                        // xe pass 1 schedule
                    }
                }

            }

        }
        return flag;

    }

    private boolean checkVehicleSchedule(ScheduleForConsignmentDTO scheduleForLocationDTO, Consignment consignment, boolean flag) {
        Long nowTime = new Date().getTime();
        long diff;
        flag = true;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<PlaceResponeDTO> listScheduleDeli =
                placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());
//
//                    //list place delivery of a consignment

        // Consignemnt
        PlaceResponeDTO placeConsignmentRecei =
                placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());


        // Schedule
        // lấy thời gian consignment  lấy hàng có độ ưu tiên 1
        // lấy thằng thời gian giao hàng sau cùng
        PlaceResponeDTO placeScheduleDeli =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), listScheduleDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

        if (placeConsignmentRecei.getAddress() != null && placeScheduleDeli.getAddress() != null) {
            diff = placeConsignmentRecei.getPlannedTime().getTime() - placeScheduleDeli.getPlannedTime().getTime();
            int diffDays = (int) diff / (24 * 60 * 60 * 1000);
            int diffHours = (int) diff / (60 * 60 * 1000) % 24;
            if (diffDays == 0) {
                if (diffHours >= 1) {
//                    checkVehicleScheduleDeli(sc)
                    flag = false;
                } else if (diffHours <= -1) {
                    List<PlaceResponeDTO> listConsignmentDeli =
                            placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());
//
//                    //list place delivery of a consignment

                    // Consignemnt
                    PlaceResponeDTO placeConsignmentDeli =
                            placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), listConsignmentDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                    // Schedule
                    // lấy thời gian consignment  lấy hàng có độ ưu tiên 1
                    // lấy thằng thời gian giao hàng sau cùng
                    PlaceResponeDTO placeScheduleRecei =
                            placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());

                    if (placeConsignmentDeli.getAddress() != null && placeScheduleRecei.getAddress() != null) {
                        diff = placeScheduleRecei.getPlannedTime().getTime() - placeConsignmentDeli.getPlannedTime().getTime();
                        diffDays = (int) diff / (24 * 60 * 60 * 1000);
                        diffHours = (int) diff / (60 * 60 * 1000) % 24;
                        if (diffDays == 0) {
                            if (diffHours >= 1) {
                                flag = false;
                            }
                        } else {

                        }


                    }
                }
            }
        }
        return flag;

    }

    private int checkVehicleScheduleDeli(ScheduleForConsignmentDTO scheduleForLocationDTO, Consignment consignment) {
        long diff;
        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<PlaceResponeDTO> listScheduleDeli =
                placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());
        PlaceResponeDTO placeConsignmentRecei =
                placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());

        PlaceResponeDTO placeScheduleDeli =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), listScheduleDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

        if (placeScheduleDeli.getAddress() != null && placeConsignmentRecei.getAddress() != null) {
            diff = placeConsignmentRecei.getPlannedTime().getTime() - placeScheduleDeli.getPlannedTime().getTime();

            int diffHours = (int) diff / (60 * 60 * 1000) % 24;

            if (diffHours >= 1) {
                result = diffHours;
            }

        }
        return result;

    }

    private List<Driver> checkScheduledForDriver(List<Driver> drivers, Consignment consignment) {
        boolean flag = true;
        List<ScheduleForConsignmentDTO> scheduleForLocationDTOS = new ArrayList<>();
        List<Driver> result = new ArrayList<>();

        List<String> dateConsignmentSchedule = new ArrayList<>();
        List<String> dateConsignmentNew = new ArrayList<>();
        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        for (int i = 0; i < drivers.size(); i++) {
            flag = true;
            double weight = consignment.getWeight();
            //check xe co lich bao tri trong tuong lai
            scheduleForLocationDTOS = checkScheduleForDriver(drivers.get(i).getId());
            if (scheduleForLocationDTOS.size() > 0) {
                for (int j = 0; j < scheduleForLocationDTOS.size(); j++) {

                    scheduleForLocationDTO = scheduleForLocationDTOS.get(j);

                    flag = checkDateConsignmentAndSchedule(scheduleForLocationDTO, consignment, flag);
//                    //list place delivery of a schedule

                    if (flag) {
                        j = scheduleForLocationDTOS.size();
                    }
                }
                if (!flag) {
                    result.add(drivers.get(i));

                }
            } else {
                result.add(drivers.get(i));
            }
        }

        return result;
    }

    @Override

    public List<Schedule> findByConsignmentStatusAndUsername(List<Integer> status, String username){


        return scheduleRepository.findByConsignmentStatusAndUsername(status, username);
    }

    @Override
    public Schedule findById(Integer id) {
        if (!scheduleRepository.existsById(id)){
            return null;
        }
        return scheduleRepository.findById(id).get();
    }

    @Override
    public Schedule findScheduleByVehDriCon(ObejctScheDTO obejctScheDTO) {
        return scheduleRepository.
                findScheduleByVeDriCons(obejctScheDTO.getDriver_id(), obejctScheDTO.getVehicle_id(), obejctScheDTO.getConsignment_id());
    }


    // ----------------------------------------------
    @Override
    public List<ScheduleForConsignmentDTO> findScheduleForFuture(List<Vehicle> vehicles, Consignment consignment, ConsignmentRequestDTO consignmentRequestDTO) {
        List<Schedule> schedules = scheduleRepository.findAll();
        boolean flag = true;
        List<ScheduleForConsignmentDTO> scheduleForLocationDTOS = new ArrayList<>();
        List<Vehicle> result = new ArrayList<>();
        List<ScheduleForConsignmentDTO> scheduleResult = new ArrayList<>();

        ScheduleForConsignmentDTO scheduleForLocationDTO = new ScheduleForConsignmentDTO();
        for (int i = 0; i < vehicles.size(); i++) {
            flag = true;
            scheduleForLocationDTOS = checkScheduleForVehicle(vehicles.get(i).getId());
            if (scheduleForLocationDTOS.size() > 0) {
                for (int j = 0; j < scheduleForLocationDTOS.size(); j++) {

                    scheduleForLocationDTO = scheduleForLocationDTOS.get(j);
                    flag = checkVehicleSchedule(scheduleForLocationDTO, consignment, flag);
                    if (!flag) {
                        if (j == scheduleForLocationDTOS.size()) {
//                            scheduleForLocationDTO.setVehicle_id(vehicles.get(i).getId());
//                            scheduleResult.addAll(scheduleForLocationDTOS);
                        }
                    } else {
                        j = scheduleForLocationDTOS.size();
                    }
                }
                if (!flag) {
                    int min = checkVehicleScheduleDeli(scheduleForLocationDTOS.get(0), consignment);
                    scheduleForLocationDTO = scheduleForLocationDTOS.get(0);
                    for (int j = 1; j < scheduleForLocationDTOS.size(); j++) {
                        int tmp = checkVehicleScheduleDeli(scheduleForLocationDTOS.get(j), consignment);
                        if (min > tmp) {
                            min = tmp;
                            scheduleForLocationDTO = scheduleForLocationDTOS.get(j);
                        }

                    }
//                    scheduleForLocationDTO.setVehicle_id(vehicles.get(i).getId());
                    scheduleResult.add(scheduleForLocationDTO);
                }

            }
        }

        return scheduleResult;
    }


}

