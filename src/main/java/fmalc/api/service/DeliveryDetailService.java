package fmalc.api.service;

import fmalc.api.entity.DeliveryDetail;
import fmalc.api.entity.Place;

public interface DeliveryDetailService {
    Place getDeliveryByConsignmentAndPriority(int idConsignment, int priority, int typlePlace);
}
