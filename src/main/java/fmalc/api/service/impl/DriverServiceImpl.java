package fmalc.api.service.impl;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.DriverLicenseEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.repository.AccountRepository;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.FleetManagerRepository;
import fmalc.api.repository.RoleRepository;
import fmalc.api.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
    @Autowired
    private DriverRepository driverRepository;

    @Override
    public Paging findAllAndSearch(String searchPhone, int pageCurrent) {
        Paging paging = new Paging();
//        Paging paging = new Paging();
        Pageable pageable = PageRequest.of(pageCurrent, paging.getNumberElements(), Sort.by("status").descending().and(Sort.by("id").descending()));
        Page page = driverRepository.findByPhoneNumberContainingIgnoreCaseOrderByIdDesc(searchPhone,pageable);
        paging.setList(new DriverResponseDTO().mapToListResponse(page.getContent()));
        paging.setTotalPage(page.getTotalPages());
        paging.setPageCurrent(pageCurrent);
        return paging;
    }

    @Override
    public Paging findAllAndSearchByFleet(int idFleet,String searchPhone, int pageCurrent) {
//        return
        Paging paging = new Paging();
//        Paging paging = new Paging();
        Pageable pageable = PageRequest.of(pageCurrent, paging.getNumberElements(), Sort.by("status").descending().and(Sort.by("id").descending()));
        Page page = driverRepository.findByFleetManagerIdAndPhoneNumberContainingIgnoreCaseOrderByIdDesc(idFleet,searchPhone, pageable);
        paging.setList(new DriverResponseDTO().mapToListResponse(page.getContent()));
        paging.setTotalPage(page.getTotalPages());
        paging.setPageCurrent(pageCurrent);
        return paging;
    }

    @Override
    public Driver findDiverByPhoneNumberOrIdentityNoOrNo(String phone, String indentityNo, String no) {
        return driverRepository.findDiverByPhoneNumberOrIdentityNoOrNo(phone,indentityNo,no);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PlaceService placeService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    DayOffService dayOffService;


    @Autowired
    MaintenanceService maintainanceService;

    @Autowired
    private UploaderService uploaderService;


    @Autowired
    private FleetManagerRepository fleetManagerRepository;

    private static int PRIOTITY_ONE = 1;

    @Override
    public Driver findById(Integer id) {
        if (driverRepository.existsById(id)) {
            return driverRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public Driver save(DriverRequestDTO driverRequest, MultipartFile file) throws IOException {
        ModelMapper modelMapper = new ModelMapper();
        Driver driver = modelMapper.map(driverRequest, Driver.class);
        Role role = roleRepository.findByRole("ROLE_DRIVER");

        Account account = new Account();
        account.setUsername(driver.getPhoneNumber());
        // To do random password
        account.setPassword(passwordEncoder.encode("123456"));
        account.setRole(role);
        account.setIsActive(true);
        account = accountRepository.save(account);
        FleetManager fleetManager = fleetManagerRepository.findById(driverRequest.getFleetManagerId()).get();

        driver.setId(null);
        driver.setAccount(account);
        String link = uploaderService.upload(file);
        driver.setImage(link);
        driver.setWorkingHour(0f);
        driver.setFleetManager(fleetManager);
        driverRepository.save(driver);

        return driver;
    }

    @Override
    public Driver update(Integer id, DriverRequestDTO driverRequest) throws Exception {
        if (!driverRepository.existsById(id)) {
            throw new Exception();
        }
        Driver driver = driverRepository.findById(id).get();
        driver.setName(driverRequest.getName());
        driver.setDateOfBirth(driverRequest.getDateOfBirth());
        driver.setIdentityNo(driverRequest.getIdentityNo());
        driver.setNo(driverRequest.getNo());
        driver.setDriverLicense(driverRequest.getDriverLicense());
        driver.setLicenseExpires(driverRequest.getLicenseExpires());
        driver.setFleetManager(fleetManagerRepository.findById(driverRequest.getFleetManagerId()).get());
        return driverRepository.save(driver);
    }

    @Override
    public List<Driver> findDriverByLicense(double weight) {
        List<Driver> drivers = new ArrayList<>();
        if (weight > 3.5) {
            drivers = driverRepository.findDriverByLicenseC(DriverLicenseEnum.C.getValue(), true);
        } else {
            drivers = driverRepository.findDriverByLicenseB2(DriverLicenseEnum.B2.getValue(),true);
        }
        return drivers;
    }

    @Override
    public List<Driver> getListDriverByLicense(double weight, int status) {
        List<Driver> drivers = new ArrayList<>();
        if (weight > 3.5) {
            drivers = driverRepository.findByDriverLicenseC(DriverLicenseEnum.C.getValue(), status);
        } else {
            drivers = driverRepository.findByDriverLicenseB2(DriverLicenseEnum.B2.getValue(), status);
        }
        return drivers;
    }

    @Override
    public int updateStatus(int status, int id) {
        return driverRepository.updateStatusDriver(status, id);
    }

    @Override
    public Driver updateAvatar(Integer id, MultipartFile file) throws IOException {
        String image = uploaderService.upload(file);
        driverRepository.updateImageById(id, image);
        return driverRepository.findById(id).get();
    }

    @Override
    public Driver findDriverByUsername(String username) {

        return driverRepository.findDriverByUsername(username);
    }

    @Override
    public List<Driver> findDriverForSchedule(double weight, Consignment consignment) {
        List<Driver> drivers = findDriverByLicense(weight);
        if (drivers.size() > 0) {
            drivers = checkMaintainForDriver(drivers, consignment);
            drivers = checkScheduledForDriver(drivers, consignment);
            drivers = dayOffService.checkDayOffODriver(drivers, consignment);
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

    @Override
    public List<ScheduleForConsignmentDTO> checkScheduleForDriver(int idDriver) {
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
                    deliveryDetail = placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTOS.get(i).getConsignment().getId(), PRIOTITY_ONE, TypeLocationEnum.RECEIVED_PLACE.getValue());
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
    public String updateTokenDevice(Driver driver) {
        return driverRepository.save(driver).getTokenDevice();
    }

    @Override
    public String findTokenDeviceByDriverId(Integer id) {

        if (driverRepository.existsById(id)) {
            return driverRepository.findTokenDeviceByDriverId(id);
        }

        return "";
    }

    @Override
    public void createDayOff(DayOffRequestDTO dayOffRequestDTO) {
        DayOff dayOff = new DayOff();
        dayOff.setStartDate(dayOffRequestDTO.getStartDate());
        dayOff.setEndDate(dayOffRequestDTO.getEndDate());
        Driver driver = driverRepository.findById(dayOffRequestDTO.getDriverId()).get();
        dayOff.setDriver(driver);
        FleetManager fleetManager = fleetManagerRepository.findById(dayOffRequestDTO.getFleetManagerId()).get();
        dayOff.setFleetManager(fleetManager);
        dayOffService.save(dayOff);
    }

    @Override
    public boolean checkIdentityNo(String identityNo) {
        return driverRepository.existsByIdentityNo(identityNo);
    }

    @Override
    public boolean checkNo(String no) {
        return driverRepository.existsByNo(no);
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

    private boolean checkDateConsignmentAndSchedule(ScheduleForConsignmentDTO scheduleForLocationDTO, Consignment consignment, boolean flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        flag = true;
        List<Place> places = (List<Place>) consignment.getPlaces();
        List<PlaceResponeDTO> listScheduleDeli =
                placeService.getPlaceByTypePlace(scheduleForLocationDTO.getConsignment().getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());
//
//                    //list place delivery of a consignment
        List<PlaceResponeDTO> listConsginmentDeli =
                getPlaceByTypePlace(places, TypeLocationEnum.DELIVERED_PLACE.getValue());

        // Consignemnt
        PlaceResponeDTO placeConsignmentRecei =
                getPlaceByTypePlaceAndPriority(places, 1, TypeLocationEnum.RECEIVED_PLACE.getValue());

        PlaceResponeDTO placeConsignmentDeli =
                getPlaceByTypePlaceAndPriority(places, listConsginmentDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

        // Schedule
        // lấy thời gian consignment  lấy hàng có độ ưu tiên 1
        PlaceResponeDTO placeScheduleRecei =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());
        // lấy thằng thời gian giao hàng sau cùng
        PlaceResponeDTO placeScheduleDeli =
                placeService.getPlaceByTypePlaceAndPriority(scheduleForLocationDTO.getConsignment().getId(), listScheduleDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());

        if (placeConsignmentRecei.getAddress() != null && placeScheduleRecei.getAddress() != null) {
            String dateReceiConsignment = sdf.format(placeConsignmentRecei.getPlannedTime());
            String dateScheduleRecei = sdf.format(placeScheduleRecei.getPlannedTime());
            if (dateReceiConsignment.compareTo(dateScheduleRecei) >= 1) {
                if (placeScheduleDeli.getAddress() != null) {
                    String dateScheduleDe = sdf.format(placeScheduleDeli.getPlannedTime());
                    if (dateReceiConsignment.compareTo(dateScheduleDe) >= 1) {
                        flag = false;
                    }
                }
            } else if (dateReceiConsignment.compareTo(dateScheduleRecei) <= -1) {
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

    private List<Driver> checkMaintainForDriver(List<Driver> drivers, Consignment consignment) {

        boolean flag = true;
        List<MaintainCheckDTO> maintainCheckDTO = new ArrayList<>();
        List<Driver> result = new ArrayList<>();

        for (int i = 0; i < drivers.size(); i++) {
            flag = false;
            //            VehicleForDetailDTO vehicle = vehicleService.findVehicleById(drivers.get(i).getId());
            //check xe co lich bao tri trong tuong lai
            maintainCheckDTO = maintainanceService.checkMaintainForDriver(drivers.get(i).getId());

            for(int m =0; m< maintainCheckDTO.size();m++){
                if(maintainCheckDTO.get(m).getActualMaintainDate()!=null){
                    flag = checkDateMaintain(consignment, maintainCheckDTO.get(m), flag);
                }

                if(flag){
                    m= maintainCheckDTO.size();
                }else{
                    flag= false;

                }
            }

            if (!flag) {
                result.add(drivers.get(i));
            }
        }

        return result;
    }

    private boolean checkDateMaintain(Consignment consignment, MaintainCheckDTO maintainCheckDTO, boolean flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<Place> places = (List<Place>) consignment.getPlaces();
        PlaceResponeDTO placeSchedulePriorityRecei =
                getPlaceByTypePlaceAndPriority(places, 1, TypeLocationEnum.DELIVERED_PLACE.getValue());

        //list place delivery of a consignment
        List<PlaceResponeDTO> placeConsgimentsPriorityDeli =
                getPlaceByTypePlace(places, TypeLocationEnum.DELIVERED_PLACE.getValue());

        //receive and priority =1
        if (placeSchedulePriorityRecei != null) {
            String dateReceiConsignment = sdf.format(placeSchedulePriorityRecei.getPlannedTime());
            String dateMaintain = sdf.format(maintainCheckDTO.getPlannedMaintainDate());
            if (dateReceiConsignment.compareTo(dateMaintain) <= 1) {
                PlaceResponeDTO placeSchedulePriorityDeli =
                        getPlaceByTypePlaceAndPriority(places, placeConsgimentsPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());
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

    @Override
    public List<Driver> findAllByFleetManager(Integer id) {
        return driverRepository.findAllByFleetManager_Id(id);
    }

}



