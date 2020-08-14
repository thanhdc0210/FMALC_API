package fmalc.api.service;

import fmalc.api.dto.PlaceResponeDTO;

import java.text.ParseException;
import java.util.List;

public interface PlaceService {
    List<PlaceResponeDTO> getPlaceOfConsignment(int idConsignment);

    List<PlaceResponeDTO> getPlaceByPriority(int idConsignment, int priority);

    PlaceResponeDTO getPlaceByTypePlaceAndPriority(int idConsignment, int priority, int typePlace);
    List<PlaceResponeDTO> getPlaceByTypePlace(int idConsignment,  int typePlace);
    PlaceResponeDTO updateActualTime(int id, int idSchedule) throws ParseException;
}
