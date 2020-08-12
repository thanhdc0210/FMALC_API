package fmalc.api.service.impl;

import fmalc.api.dto.PlaceResponeDTO;
import fmalc.api.entity.*;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.PlaceRepository;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    LocationService locationService;

    @Autowired
    DriverService driverService;
    @Autowired
    ScheduleService scheduleService;

    @Autowired
    DriverRepository driverRepository;

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;

    @Override
    public List<PlaceResponeDTO> getPlaceOfConsignment(int idConsignment) {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        List<PlaceResponeDTO> placeResponeDTOS = new ArrayList<>();
        List<Place> places =placeRepository.getPlaceOfConsignment(idConsignment);
        if(places.size()>0){
            placeResponeDTOS = placeResponeDTO.mapToListResponse(places);
        }
//        placeResponeDTOS = placeResponeDTO.mapToListResponse();
        return  placeResponeDTOS;
    }

    @Override
    public List<PlaceResponeDTO> getPlaceByPriority(int idConsignment, int priority) {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        List<PlaceResponeDTO> placeResponeDTOS = new ArrayList<>();
        List<Place> places = placeRepository.getPlaceByPriority(idConsignment, priority);
        if(places.size()>0){
            placeResponeDTOS = placeResponeDTO.mapToListResponse(places);
        }
//        placeResponeDTOS = placeResponeDTO.mapToListResponse();
        return  placeResponeDTOS;
    }

    @Override
    public List<PlaceResponeDTO> getPlaceByTypePlace(int idConsignment, int typePlace) {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        List<PlaceResponeDTO> placeResponeDTOS = new ArrayList<>();
        List<Place> places = placeRepository.getPlaceByTypePlace(idConsignment, typePlace);
        if(places.size()>0){
            placeResponeDTOS = placeResponeDTO.mapToListResponse(places);
        }

        return  placeResponeDTOS;
    }

    @Override
    public PlaceResponeDTO updateActualTime(int id,int idSchedule) throws ParseException {
        Place place = placeRepository.findById(id);
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        if(place!=null){
//            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.getDefault());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            place.setActualTime(timestamp);
            place = placeRepository.save(place);
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat();
            placeResponeDTO = placeResponeDTO.convertPlace(place);
            Consignment consignment = consignmentService.findById(place.getConsignment().getId());
            List<Schedule> schedules = (List<Schedule>) consignment.getSchedules();
            List<Place> places = (List<Place>) consignment.getPlaces();
            List<Location> locations = new ArrayList<>();
            Schedule schedule = new Schedule();
            if(places.size()>0){
                places.sort(Comparator.comparing(Place::getPlannedTime));
                if(places.get(places.size()-1).getActualTime()!= null){

                        schedule = scheduleService.findById(idSchedule);
                        if(schedule.getInheritance()== null ){
                            locations.addAll(locationService.getListLocationBySchedule(idSchedule));
//                            schedule = schedules.get(i);

                        }

                    if(locations.size()>0){
                        locations.sort(Comparator.comparing(Location::getTime));
                        long diff = places.get(places.size()-1).getActualTime().getTime() - locations.get(0).getTime().getTime();
                        float hours = (float) (diff/MINUTE);
                        if(hours>0){
                            Driver driver = driverService.findById(schedule.getDriver().getId());
                            driver.setWorkingHour(hours);
                            driver = driverRepository.save(driver);
                        }
                    }
                }
            }

        }
        return placeResponeDTO;
    }

    @Override
    public PlaceResponeDTO getPlaceByTypePlaceAndPriority(int idConsignment, int priority, int typePlace) {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        Place place = placeRepository.getPlaceByTypePlaceAndPriority(idConsignment, priority, typePlace);
        if(place!= null){
            placeResponeDTO = placeResponeDTO.convertPlace(place);
        }

        return  placeResponeDTO;
    }
}
