package fmalc.api.service.impl;

import fmalc.api.entity.DeliveryDetail;
import fmalc.api.entity.Place;
import fmalc.api.repository.DeliveryDetailRepository;
import fmalc.api.service.DeliveryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryDetailServiceImpl implements DeliveryDetailService {

    @Autowired
    DeliveryDetailRepository deliveryDetailRepository;

    @Override
    public Place getDeliveryByConsignmentAndPriority(int idConsignment, int priority, int typlePlace) {

        return  deliveryDetailRepository.findDeliveryDetailByConsignmentID(idConsignment,priority,typlePlace);
    }
}
