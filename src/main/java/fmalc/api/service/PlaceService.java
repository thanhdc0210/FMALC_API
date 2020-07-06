package fmalc.api.service;

import fmalc.api.dto.PlaceResponeDTO;
import fmalc.api.entity.Place;

import java.util.*;

public interface PlaceService {
    List<PlaceResponeDTO> getPlaceOfConsignment(int idConsignment);

    List<PlaceResponeDTO> getPlaceByPriority(int idConsignment, int priority);

    PlaceResponeDTO getPlaceByTypePlaceAndPriority(int idConsignment, int priority, int typePlace);
    List<PlaceResponeDTO> getPlaceByTypePlace(int idConsignment,  int typePlace);
}
