package fmalc.api.service.impl;

import fmalc.api.dto.*;
import fmalc.api.entity.*;
import fmalc.api.enums.ConsignmentStatusEnum;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.repository.*;
import fmalc.api.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    private ConsignmentRepository consignmentRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    DriverRepository driverRepository;

    @Autowired
    FleetManagerService fleetManagerService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    DriverService driverService;

    @Autowired
    AccountService accountService;

    @Autowired
    ConsignmentHistoryService consignmentHistoryService;

    private static final String ADMIN = "ROLE_ADMIN";
    private static final String FLEET = "ROLE_FLEET_MANAGER";

//    @Override
//    public List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(List<Integer> status, String username) {
//        return consignmentRepository.findByConsignmentStatusAndUsernameForFleetManager(status, username);
//    }

    @Override
    public Consignment findById(int consignment_id) {
        return consignmentRepository.findById(consignment_id).get();
    }

    @Override
    public Consignment save(ConsignmentRequestDTO consignmentRequestDTO) throws ParseException {
        ModelMapper modelMapper = new ModelMapper();
        Consignment consignment = modelMapper.map(consignmentRequestDTO, Consignment.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(""));
        List<Place> places = consignmentRequestDTO.getPlace().stream()
                .map(x -> modelMapper.map(x, Place.class))
                .collect(Collectors.toList());
        consignment.setImageContract(consignmentRequestDTO.getImageConsignment());
        consignment = consignmentRepository.save(consignment);

        for (int i = 0; i < places.size(); i++) {
            places.get(i).setConsignment(consignment);
            placeRepository.save(places.get(i));
        }

        consignment.setPlaces(places);
        return consignment;
    }

    @Override
    public List<Consignment> findAll() {
        return consignmentRepository.findAll();
    }

    private List<ConsignmentListDTO> detailList(List<ConsignmentListDTO> consignmentListDTOS) {
        for (int i = 0; i < consignmentListDTOS.size(); i++) {
            List<ScheduleForLocationDTO> schedules = new ArrayList<>();
            schedules = scheduleService.getScheduleByConsignmentId(consignmentListDTOS.get(i).getId());
            if (schedules.size() > 0) {
                for (int j = 0; j < schedules.size(); j++) {
                    if (schedules.get(j).isApprove()) {
                        VehicleForDetailDTO vehicleForDetailDTO;
                        List<VehicleForDetailDTO> vehicleForDetailDTOS = new ArrayList<>();

                        Driver driver = new Driver();
                        List<Driver> drivers = new ArrayList<>();

                        DriverResponseDTO driverResponseDTO = new DriverResponseDTO();
                        List<DriverResponseDTO> driverResponseDTOS = new ArrayList<>();

                        vehicleForDetailDTO = vehicleService.findVehicleById(schedules.get(j).getVehicle_id());
                        vehicleForDetailDTOS.add(vehicleForDetailDTO);

                        driver = driverService.findById(schedules.get(j).getDriver_id());
                        drivers.add(driver);
                        driverResponseDTOS = driverResponseDTO.mapToListResponse(drivers);
                        consignmentListDTOS.get(i).setDrivers(driverResponseDTOS);
                        consignmentListDTOS.get(i).setVehicles(vehicleForDetailDTOS);
                    }

                }
            }
        }
        return consignmentListDTOS;
    }

    @Override
    public Paging getAllByStatus(Integer status, String username, int type, int pageCurrent, String search) {
        Account account = accountService.getAccount(username);
        Paging paging = new Paging();
        Pageable pageable = PageRequest.of(pageCurrent, paging.getNumberElements());
        List<ConsignmentListDTO> consignmentListDTOS = new ArrayList<>();
        if (account.getRole().getRole().equals(ADMIN)) {
            if (type == 0) {
                if (search != "") {
                    try {
                        int id = Integer.parseInt(search);
                        if (status == ConsignmentStatusEnum.OBTAINING.getValue() || status == ConsignmentStatusEnum.DELIVERING.getValue()) {

                            Page page = consignmentRepository.findConsignmentByStatusIdObOrDe(id,ConsignmentStatusEnum.OBTAINING.getValue(),ConsignmentStatusEnum.DELIVERING.getValue(),  pageable);
                            consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                            if (consignmentListDTOS.size() > 0) {
                                if (paging.getList() != null) {
                                    paging.getList().addAll(consignmentListDTOS);
                                } else {
                                    paging.setList(consignmentListDTOS);
                                }
                            } else {
                                paging.setList(new ArrayList());
                            }

                            paging.setTotalPage(page.getTotalPages());
                        } else {
                            Page page = consignmentRepository.findConsignmentByStatusId(id, status, pageable);
                            consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                            if (consignmentListDTOS.size() > 0) {
                                if (paging.getList() != null) {
                                    paging.getList().addAll(consignmentListDTOS);
                                } else {
                                    paging.setList(consignmentListDTOS);
                                }
                            } else {
                                paging.setList(new ArrayList());
                            }
                            paging.setTotalPage(page.getTotalPages());
                        }
                        paging.setPageCurrent(pageCurrent);
                        return paging;
                    } catch (NumberFormatException e) {
                        return paging;
                    }
                } else {
                    if (status == ConsignmentStatusEnum.OBTAINING.getValue() || status == ConsignmentStatusEnum.DELIVERING.getValue()) {
                        Page page = consignmentRepository.findAllByStatusObOrDe(ConsignmentStatusEnum.OBTAINING.getValue(),ConsignmentStatusEnum.DELIVERING.getValue(), pageable);
                        consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                        if (consignmentListDTOS.size() > 0) {
                            if (paging.getList() != null) {
                                paging.getList().addAll(consignmentListDTOS);
                            } else {
                                paging.setList(consignmentListDTOS);
                            }
                        } else {
                            paging.setList(new ArrayList());
                        }

                        paging.setTotalPage(page.getTotalPages());
                    } else {
                        Page page = consignmentRepository.findAllByStatus(status, pageable);
                        consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                        if (consignmentListDTOS.size() > 0) {
                            if (paging.getList() != null) {
                                paging.getList().addAll(consignmentListDTOS);
                            } else {
                                paging.setList(consignmentListDTOS);
                            }
                        } else {
                            paging.setList(new ArrayList());
                        }

                        paging.setTotalPage(page.getTotalPages());
                    }
                    paging.setPageCurrent(pageCurrent);
                    return paging;
                }


            } else {
                if (status == ConsignmentStatusEnum.OBTAINING.getValue() || status == ConsignmentStatusEnum.DELIVERING.getValue()) {
                    Page page = consignmentRepository.findConsignmentByStatusAndOwnerNameIsContaining(ConsignmentStatusEnum.OBTAINING.getValue() ,ConsignmentStatusEnum.DELIVERING.getValue(), search, pageable);
                    consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                    if (consignmentListDTOS.size() > 0) {
                        if (paging.getList() != null) {
                            paging.getList().addAll(consignmentListDTOS);
                        } else {
                            paging.setList(consignmentListDTOS);
                        }
                    } else {
                        paging.setList(new ArrayList());
                    }
//                   paging.setTotalPage(page.getTotalPages());
//                    Page page2 = consignmentRepository.findConsignmentByStatusAndOwnerNameIsContaining(ConsignmentStatusEnum.OBTAINING.getValue(),ConsignmentStatusEnum.DELIVERING.getValue(), search, pageable);
//                    consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page2.getContent()));
//                    if (consignmentListDTOS.size() > 0) {
//                        if (paging.getList() != null) {
//                            paging.getList().addAll(consignmentListDTOS);
//                        } else {
//                            paging.setList(consignmentListDTOS);
//                        }
//                    } else {
//                        paging.setList(new ArrayList());
//                    }
                    paging.setTotalPage(page.getTotalPages());
                } else {
                    Page page = consignmentRepository.findConsignmentByStatus(status, search, pageable);
                    consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                    if (consignmentListDTOS.size() > 0) {
                        if (paging.getList() != null) {
                            paging.getList().addAll(consignmentListDTOS);
                        } else {
                            paging.setList(consignmentListDTOS);
                        }
                    } else {
                        paging.setList(new ArrayList());
                    }
                    paging.setTotalPage(page.getTotalPages());
                }

                paging.setPageCurrent(pageCurrent);
                return paging;
            }
        } else if(account.getRole().getRole().equals(FLEET)) {
            if (type == 0) {
                if (search != "") {
                    try {
                        int id = Integer.parseInt(search);
                        if (status == ConsignmentStatusEnum.OBTAINING.getValue() || status == ConsignmentStatusEnum.DELIVERING.getValue()) {

                            Page page = consignmentRepository.findConsignmentByStatusIdFleetObOrDe(id,ConsignmentStatusEnum.OBTAINING.getValue(),ConsignmentStatusEnum.DELIVERING.getValue(),account.getId(),  pageable);
                            consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                            if (consignmentListDTOS.size() > 0) {
                                if (paging.getList() != null) {
                                    paging.getList().addAll(consignmentListDTOS);
                                } else {
                                    paging.setList(consignmentListDTOS);
                                }
                            } else {
                                paging.setList(new ArrayList());
                            }
//                            Page page2 = consignmentRepository.findConsignmentByStatusIdFleet(id,ConsignmentStatusEnum.DELIVERING.getValue(),account.getId(),  pageable);
//                            consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page2.getContent()));
//                            if (consignmentListDTOS.size() > 0) {
//                                if (paging.getList() != null) {
//                                    paging.getList().addAll(consignmentListDTOS);
//                                } else {
//                                    paging.setList(consignmentListDTOS);
//                                }
//                            } else {
//                                paging.setList(new ArrayList());
//                            }
                            paging.setTotalPage(page.getTotalPages());
                        } else {
                            Page page = consignmentRepository.findConsignmentByStatusIdFleet(id, status,account.getId(), pageable);
                            consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                            if (consignmentListDTOS.size() > 0) {
                                if (paging.getList() != null) {
                                    paging.getList().addAll(consignmentListDTOS);
                                } else {
                                    paging.setList(consignmentListDTOS);
                                }
                            } else {
                                paging.setList(new ArrayList());
                            }
                            paging.setTotalPage(page.getTotalPages());
                        }
                        paging.setPageCurrent(pageCurrent);
                        return paging;
                    } catch (NumberFormatException e) {
                        return paging;
                    }
                } else {
                    if (status == ConsignmentStatusEnum.OBTAINING.getValue() || status == ConsignmentStatusEnum.DELIVERING.getValue()) {
                        Page page = consignmentRepository.findAllByStatusFleetObOrDe(ConsignmentStatusEnum.OBTAINING.getValue(),ConsignmentStatusEnum.DELIVERING.getValue(),account.getId(), pageable);
                        consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                        if (consignmentListDTOS.size() > 0) {
                            if (paging.getList() != null) {
                                paging.getList().addAll(consignmentListDTOS);
                            } else {
                                paging.setList(consignmentListDTOS);
                            }
                        } else {
                            paging.setList(new ArrayList());
                        }
//                        Page page2 = consignmentRepository.findAllByStatusFleet(ConsignmentStatusEnum.OBTAINING.getValue(),account.getId(), pageable);
//                        consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
//                        if (consignmentListDTOS.size() > 0) {
//                            if (paging.getList() != null) {
//                                paging.getList().addAll(consignmentListDTOS);
//                            } else {
//                                paging.setList(consignmentListDTOS);
//                            }
//                        } else {
//                            paging.setList(new ArrayList());
//                        }
                        paging.setTotalPage(page.getTotalPages());
                    } else {
                        Page page = consignmentRepository.findAllByStatusFleet(status,account.getId(), pageable);
                        consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                        if (consignmentListDTOS.size() > 0) {
                            if (paging.getList() != null) {
                                paging.getList().addAll(consignmentListDTOS);
                            } else {
                                paging.setList(consignmentListDTOS);
                            }
                        } else {
                            paging.setList(new ArrayList());
                        }

                        paging.setTotalPage(page.getTotalPages());
                    }
                    paging.setPageCurrent(pageCurrent);
                    return paging;
                }


            } else {
                if (status == ConsignmentStatusEnum.OBTAINING.getValue() || status == ConsignmentStatusEnum.DELIVERING.getValue()) {
                    Page page = consignmentRepository.findConsignmentByStatusObOrDe(ConsignmentStatusEnum.OBTAINING.getValue(),ConsignmentStatusEnum.DELIVERING.getValue(), search,account.getId(), pageable);
                    consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                    if (consignmentListDTOS.size() > 0) {
                        if (paging.getList() != null) {
                            paging.getList().addAll(consignmentListDTOS);
                        } else {
                            paging.setList(consignmentListDTOS);
                        }
                    } else {
                        paging.setList(new ArrayList());
                    }
//                   paging.setTotalPage(page.getTotalPages());
//                    Page page2 = consignmentRepository.findConsignmentByStatusAndOwnerNameIsContainingFleet(status, search,account.getId(), pageable);
//                    consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
//                    if (consignmentListDTOS.size() > 0) {
//                        if (paging.getList() != null) {
//                            paging.getList().addAll(consignmentListDTOS);
//                        } else {
//                            paging.setList(consignmentListDTOS);
//                        }
//                    } else {
//                        paging.setList(new ArrayList());
//                    }
                    paging.setTotalPage(page.getTotalPages());
                } else {
                    Page page = consignmentRepository.findConsignmentByStatusAndOwnerNameIsContainingFleet(status, search,account.getId(), pageable);
                    consignmentListDTOS = detailList(new ConsignmentListDTO().mapToListResponse(page.getContent()));
                    if (consignmentListDTOS.size() > 0) {
                        if (paging.getList() != null) {
                            paging.getList().addAll(consignmentListDTOS);
                        } else {
                            paging.setList(consignmentListDTOS);
                        }
                    } else {
                        paging.setList(new ArrayList());
                    }
                    paging.setTotalPage(page.getTotalPages());
                }

                paging.setPageCurrent(pageCurrent);
                return paging;
            }
        }
        return paging;
    }

    @Override
    public Consignment consignmentConfirm(ConsignmentRequestDTO consignmentRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Consignment consignment = modelMapper.map(consignmentRequestDTO, Consignment.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(""));
        List<Place> place = consignmentRequestDTO.getPlace().stream()
                .map(x -> modelMapper.map(x, Place.class))
                .collect(Collectors.toList());
//        consignment = consignmentRepository.save(consignment);
        List<Place> placesRecei = new ArrayList<>();
        List<Place> placesDeli = new ArrayList<>();
        List<Place> places = new ArrayList<>();
//        if(consignment.getId()!=null){
        for (int i = 0; i < place.size(); i++) {

            if (place.get(i).getType() == TypeLocationEnum.RECEIVED_PLACE.getValue()) {
                placesRecei.add(place.get(i));

            } else if (place.get(i).getType() == TypeLocationEnum.DELIVERED_PLACE.getValue()) {
                placesDeli.add(place.get(i));
            }
//                place.get(i).setConsignment(consignment);
//                place.get(i).setPriority(1);
//                placeRepository.save(place.get(i));
        }

        placesRecei.sort((Place s1, Place s2) -> (s2.getPlannedTime()).compareTo((s1.getPlannedTime())));
        placesDeli.sort((Place s1, Place s2) -> (s2.getPlannedTime()).compareTo((s1.getPlannedTime())));

        for (int i = 0; i < placesRecei.size(); i++) {
            placesRecei.get(i).setPriority(placesRecei.size() - i);
            placesRecei.get(i).setConsignment(consignment);
        }
        for (int i = 0; i < placesDeli.size(); i++) {
            placesDeli.get(i).setPriority(placesDeli.size() - i);
            placesDeli.get(i).setConsignment(consignment);
        }
        places.addAll(placesRecei);
        places.addAll(placesDeli);
        for (int i = 0; i < places.size(); i++) {
            String dateTemp = sdf.format(places.get(i).getPlannedTime());
            dateTemp = dateTemp.replace("T", " ");
//                dateTemp = dateTemp.replace("+", "");
            dateTemp = dateTemp.substring(0, dateTemp.indexOf("+"));
            places.get(i).setPlannedTime(Timestamp.valueOf(dateTemp));
//                placeRepository.save(places.get(i));
        }
//            consignment.setPlaces(places);
        //            for(int i =0; i<placesRecei.size(); i++){
//               List<Place> placesReceis  = placesRecei.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
//            }
//        }
        consignment.setPlaces(places);
        return consignment;
    }

    @Override
    public List<Consignment> findConsignmentByVehicle(int idVehicle) {
        List<Consignment> consignments = new ArrayList<>();
        List<Consignment> result = new ArrayList<>();
        consignments = consignmentRepository.findConsignment(idVehicle);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        String dateNow = sdf.format(date);
        List<Place> places = new ArrayList<>();
        for (int i = 0; i < consignments.size(); i++) {
            places = (List<Place>) consignments.get(i).getPlaces();
            List<Place> placeDe = placeRepository.getPlaceByTypePlace(consignments.get(i).getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());
            for (int j = 0; j < places.size(); j++) {
                if (places.get(j).getType() == TypeLocationEnum.RECEIVED_PLACE.getValue() && places.get(j).getPriority() == 1) {
                    String dateRecei = sdf.format(places.get(j).getPlannedTime());
                    if (dateNow.compareTo(dateRecei) == 0) {
                        result.add(consignments.get(i));
                        j = places.size();
                    }
                } else if (places.get(j).getType() == TypeLocationEnum.DELIVERED_PLACE.getValue() && places.get(j).getPriority() == placeDe.size()) {
                    String dateDeli = sdf.format(places.get(j).getPlannedTime());
                    if (dateNow.compareTo(dateDeli) == 0) {
                        result.add(consignments.get(i));
                        j = places.size();
                    }
                }
            }

        }
        return result;
    }

    @Override
    public int updateStatus(int status, int id) {

        if (consignmentRepository.updateStatusVehicle(status, id) > 0) {
            consignmentRepository.findById(id);
        }
        return consignmentRepository.findById(id).get().getStatus();
    }

    @Override
    public List<Consignment> getConsignmentOfDriver(int id, int status) {
        return consignmentRepository.getConsignmentOfDriver(id, status);
    }

    @Override
    public List<Schedule> findScheduleByConsignment(int id) {

        return scheduleRepository.findScheudleByConsignment(id);
    }

    @Override
    public Consignment cancelConsignment(int id, String content, String username) {
        Consignment consignment = consignmentRepository.findConsignmentById(id);
        if (consignment.getStatus() == ConsignmentStatusEnum.WAITING.getValue()) {
            consignment.setStatus(ConsignmentStatusEnum.CANCELED.getValue());
            consignment.setCancelNote(content);
        }
        consignment = consignmentRepository.save(consignment);
        Account account = accountService.getAccount(username);
        FleetManager fleetManager = fleetManagerService.findByAccount(account.getId());
        String note = "Lô hàng bị hủy bởi "+ fleetManager.getName();
        consignmentHistoryService.save(consignment.getId(), fleetManager,note);

        return consignment;
    }

    @Override
    public int updateConsignment(ConsignmentUpdateDTO consignmentUpdateDTO, String username) {
        int result = 0;
        Consignment consignment = consignmentRepository.findConsignmentById(consignmentUpdateDTO.getId());
        consignment.setOwnerName(consignmentUpdateDTO.getOwnerName());
        consignment.setOwnerNote(consignmentUpdateDTO.getOwnerNote());
        for (int i = 0; i < consignmentUpdateDTO.getSchedules().size(); i++) {
            Schedule schedule = scheduleRepository.findScheduleById(consignmentUpdateDTO.getSchedules().get(i).getId());
            Driver driver = driverRepository.findById(consignmentUpdateDTO.getSchedules().get(i).getDriver()).get();
            Vehicle vehicle = vehicleRepository.findById(consignmentUpdateDTO.getSchedules().get(i).getVehicle()).get();

            schedule.setVehicle(vehicle);
            schedule.setDriver(driver);
            schedule = scheduleRepository.save(schedule);

        }
        for (int i = 0; i < consignmentUpdateDTO.getPlaces().size(); i++) {
            Place place = placeRepository.findPlaceById(consignmentUpdateDTO.getPlaces().get(i).getId());
            place.setContactName(consignmentUpdateDTO.getPlaces().get(i).getContactName());
            place.setContactPhone(consignmentUpdateDTO.getPlaces().get(i).getContactPhone());
            place = placeRepository.save(place);

        }
        consignment = consignmentRepository.save(consignment);
        Account account = accountService.getAccount(username);
        FleetManager fleetManager = fleetManagerService.findByAccount(account.getId());

        String note ="Cập nhật lại lô hàng số "+consignmentUpdateDTO.getId();
        ConsignmentHistory consignmentHistory = consignmentHistoryService.save(consignmentUpdateDTO.getId(),fleetManager,note);

        return 0;
    }

    @Override
    public Integer findConsignmentId(Integer driverId, Integer vehicleId) {
        return scheduleRepository.findByDriver_IdAndVehicle_IdAndConsignment_StatusIn(driverId, vehicleId, Arrays.asList(1, 2)).getConsignment().getId();
    }
}
