package fmalc.api.service.impl;

import fmalc.api.dto.PlaceResponeDTO;
import fmalc.api.entity.Place;
import fmalc.api.repository.PlaceRepository;
import fmalc.api.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    PlaceRepository placeRepository;

    @Override
    public List<PlaceResponeDTO> getPlaceOfConsignment(int idConsignment) {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        List<PlaceResponeDTO> placeResponeDTOS = new ArrayList<>();
        placeResponeDTOS = placeResponeDTO.mapToListResponse(placeRepository.getPlaceOfConsignment(idConsignment));
        return  placeResponeDTOS;
    }

    @Override
    public List<PlaceResponeDTO> getPlaceByPriority(int idConsignment, int priority) {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        List<PlaceResponeDTO> placeResponeDTOS = new ArrayList<>();
        placeResponeDTOS = placeResponeDTO.mapToListResponse(placeRepository.getPlaceByPriority(idConsignment, priority));
        return  placeResponeDTOS;
    }

    @Override
    public List<PlaceResponeDTO> getPlaceByTypePlace(int idConsignment, int typePlace) {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        List<PlaceResponeDTO> placeResponeDTOS = new ArrayList<>();
        placeResponeDTOS = placeResponeDTO.mapToListResponse(placeRepository.getPlaceByTypePlace(idConsignment, typePlace));
        return  placeResponeDTOS;
    }

    @Override
    public PlaceResponeDTO getPlaceByTypePlaceAndPriority(int idConsignment, int priority, int typePlace) {
        PlaceResponeDTO placeResponeDTO = new PlaceResponeDTO();
        placeResponeDTO = placeResponeDTO.convertPlace(placeRepository.getPlaceByTypePlaceAndPriority(idConsignment, priority, typePlace));
        return  placeResponeDTO;
    }
}
