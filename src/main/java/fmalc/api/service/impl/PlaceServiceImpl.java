package fmalc.api.service.impl;

import fmalc.api.dto.PlaceResponeDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Place;
import fmalc.api.repository.PlaceRepository;
import fmalc.api.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    PlaceRepository placeRepository;

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
    public PlaceResponeDTO updateActualTime(int id) throws ParseException {
        Place place = placeRepository.findById(id);
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        if(place!=null){
//            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.getDefault());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            place.setActualTime(timestamp);
            place = placeRepository.save(place);

            placeResponeDTO = placeResponeDTO.convertPlace(place);
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
