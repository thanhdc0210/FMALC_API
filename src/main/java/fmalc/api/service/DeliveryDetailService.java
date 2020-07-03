package fmalc.api.service;

import fmalc.api.dto.DeliveryDetailResponseDTO;
import fmalc.api.entity.DeliveryDetail;
import fmalc.api.entity.Place;

import java.util.List;

public interface DeliveryDetailService {
    Place getDeliveryByConsignmentAndPriority(int idConsignment, int priority, int typlePlace);
    List<DeliveryDetailResponseDTO>
     getDeliveryDetailByConsignment(int idConsignment);
    List<Place> getListPlace(int idConsignment,  int typlePlace);
}
