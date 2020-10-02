package fmalc.api.service.impl;

import fmalc.api.dto.PlaceResponeDTO;
import fmalc.api.entity.*;
import fmalc.api.repository.DriverRepository;
import fmalc.api.repository.PlaceRepository;
import fmalc.api.repository.ScheduleRepository;
import fmalc.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    LocationService locationService;

    @Autowired
    ScheduleRepository scheduleRepository;

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
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            List<Schedule> schedules = scheduleRepository.getScheduleByConsignmentId(place.getConsignment().getId());
            Consignment consignment = consignmentService.findById(place.getConsignment().getId());
            for( int i =0; i< schedules.size();i++){
                if(schedules.get(i).getStatus() == consignment.getStatus()){
                    if(i==schedules.size()-1){
//                        if(statusToUpdateDTO.getConsignment_status()>=0){
                            place.setActualTime(timestamp);
                            place = placeRepository.save(place);
//                        }
                    }
                }else{
                    i = schedules.size();
                }

            }

            placeResponeDTO = placeResponeDTO.convertPlace(place);

            List<Place> places = (List<Place>) consignment.getPlaces();
            List<Location> locations = new ArrayList<>();
            Schedule schedule;
            if(places.size()>0){
                places.sort(Comparator.comparing(Place::getPlannedTime));
                if(places.get(places.size()-1).getActualTime()!= null){
                        schedule = scheduleService.findById(idSchedule);
                        if(schedule.getInheritance()== null ){
                            locations.addAll(locationService.getListLocationBySchedule(idSchedule));
                        }
                    if(locations.size()>0){
                        locations.sort(Comparator.comparing(Location::getTime));
                        long diff = places.get(places.size()-1).getActualTime().getTime() - locations.get(0).getTime().getTime();
                        float hours = (float) (diff/MINUTE);
                        if(hours>0){
                            Driver driver = driverService.findById(schedule.getDriver().getId());
                            driver.setWorkingHour(hours);
                            driverRepository.save(driver);
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
