package fmalc.api.service.impl;

import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.DeliveryDetail;
import fmalc.api.entity.Place;
import fmalc.api.repository.ConsignmentRepository;
import fmalc.api.service.ConsignmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Override
    public List<Consignment> findByConsignmentStatusAndUsernameForDriver(List<Integer> status, String username){

        return consignmentRepository.findByConsignmentStatusAndUsernameForDriver(status, username);
    }

    @Override
    public List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(List<Integer> status, String username) {
        return consignmentRepository.findByConsignmentStatusAndUsernameForFleetManager(status, username);
    }

    @Override
    public Consignment findById(Integer consignment_id) {
        return consignmentRepository.findById(consignment_id).get();
    }

    @Override
    public List<Consignment> findAll() {
        return consignmentRepository.findAll();
    }

    @Override
    public Consignment save(ConsignmentRequestDTO consignmentRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Consignment consignment = modelMapper.map(consignmentRequestDTO, Consignment.class);

        List<Place> place = consignmentRequestDTO.getPlace().stream()
                .map(x -> modelMapper.map(x, Place.class))
                .collect(Collectors.toList());

        List<DeliveryDetail> deliveryDetails = new ArrayList<>();
        for (int i = 0; i < place.size(); i++) {
            DeliveryDetail deliveryDetail = new DeliveryDetail();
            deliveryDetail.setPlace(place.get(i));
            deliveryDetail.setConsignment(consignment);
            deliveryDetail.setPriority(1);
            deliveryDetails.add(deliveryDetail);
        }
        consignment.setDeliveries(deliveryDetails);
        return consignmentRepository.save(consignment);
    }
}
