package fmalc.api.service.impl;

import fmalc.api.dto.DeliveryDetailResponseDTO;
import fmalc.api.entity.DeliveryDetail;
import fmalc.api.entity.Place;
import fmalc.api.repository.DeliveryDetailRepository;
import fmalc.api.service.DeliveryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryDetailServiceImpl implements DeliveryDetailService {

    @Autowired
    DeliveryDetailRepository deliveryDetailRepository;

    @Override
    public Place getDeliveryByConsignmentAndPriority(int idConsignment, int priority, int typlePlace) {

        return  deliveryDetailRepository.findDeliveryDetailByConsignmentID(idConsignment,priority,typlePlace);
    }

    @Override
    public List<DeliveryDetailResponseDTO> getDeliveryDetailByConsignment(int idConsignment) {
        DeliveryDetailResponseDTO deliveryDetailResponseDTO = new DeliveryDetailResponseDTO();
        List<DeliveryDetailResponseDTO> deliveryDetailResponseDTOS = new ArrayList<>();
        deliveryDetailResponseDTOS = deliveryDetailResponseDTO.mapToListResponse(deliveryDetailRepository.findDeliveryDetailByConsignment(idConsignment));
        return  deliveryDetailResponseDTOS;
    }

    @Override
    public List<Place> getListPlace(int idConsignment, int typlePlace) {
        return null;
    }
}
