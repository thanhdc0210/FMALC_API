<<<<<<< HEAD
package fmalc.api.service.impl;

import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.repository.ConsignmentRepository;
import fmalc.api.entity.Place;
import fmalc.api.repository.PlaceRepository;
import fmalc.api.service.ConsignmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    private ConsignmentRepository consignmentRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Override
    public List<Consignment> findByConsignmentStatusAndUsernameForDriver(List<Integer> status, String username){

        return consignmentRepository.findByConsignmentStatusAndUsernameForDriver(status, username);
    }

    @Override
    public List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(List<Integer> status, String username) {
        return consignmentRepository.findByConsignmentStatusAndUsernameForFleetManager(status, username);
    }

    @Override
    public Consignment findById(int consignment_id) {
        return consignmentRepository.findById(consignment_id);
    }

    @Override
    public Consignment save(ConsignmentRequestDTO consignmentRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Consignment consignment = modelMapper.map(consignmentRequestDTO, Consignment.class);

        List<Place> place = consignmentRequestDTO.getPlace().stream()
                .map(x -> modelMapper.map(x, Place.class))
                .collect(Collectors.toList());
        consignment = consignmentRepository.save(consignment);
        if(consignment.getId()!=null){
            for(int i= 0; i<place.size();i++){

                place.get(i).setConsignment(consignment);
                place.get(i).setPriority(1);
                placeRepository.save(place.get(i));
            }
        }
        return consignmentRepository.save(consignment);
    }

    @Override
    public List<Consignment> findAll() {
        return consignmentRepository.findAll();
    }

    @Override
    public List<Consignment> getAllByStatus(Integer status) {
        return consignmentRepository.findAllByStatus(status);
    }
}
=======
//package fmalc.api.service.impl;
//
//import fmalc.api.dto.ConsignmentRequestDTO;
//import fmalc.api.entity.Consignment;
//import fmalc.api.entity.Place;
//import fmalc.api.repository.ConsignmentRepository;
//import fmalc.api.service.ConsignmentService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ConsignmentServiceImpl implements ConsignmentService {
//
//    @Autowired
//    private ConsignmentRepository consignmentRepository;
//
//    @Override
//    public List<Consignment> findByConsignmentStatusAndUsernameForDriver(List<Integer> status, String username){
//
//        return consignmentRepository.findByConsignmentStatusAndUsernameForDriver(status, username);
//    }
//
//    @Override
//    public List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(List<Integer> status, String username) {
//        return consignmentRepository.findByConsignmentStatusAndUsernameForFleetManager(status, username);
//    }
//
//    @Override
//    public Consignment findById(int consignment_id) {
//        return consignmentRepository.findById(consignment_id);
//    }
//
//    @Override
//    public Consignment save(ConsignmentRequestDTO consignmentRequestDTO) {
//        ModelMapper modelMapper = new ModelMapper();
//        Consignment consignment = modelMapper.map(consignmentRequestDTO, Consignment.class);
//
//        List<Place> place = consignmentRequestDTO.getPlace().stream()
//                .map(x -> modelMapper.map(x, Place.class))
//                .collect(Collectors.toList());
//
//        List<DeliveryDetail> deliveryDetails = new ArrayList<>();
//        for (int i = 0; i < place.size(); i++) {
//            DeliveryDetail deliveryDetail = new DeliveryDetail();
//            deliveryDetail.setPlace(place.get(i));
//            deliveryDetail.setConsignment(consignment);
//            deliveryDetail.setPriority(1);
//            deliveryDetails.add(deliveryDetail);
//        }
//        consignment.setDeliveries(deliveryDetails);
//        return consignmentRepository.save(consignment);
//    }
//
//    @Override
//    public List<Consignment> findAll() {
//        return consignmentRepository.findAll();
//    }
//
//    @Override
//    public List<Consignment> getAllByStatus(Integer status) {
//        return consignmentRepository.findAllByStatus(status);
//    }
//}
>>>>>>> 0c51869f5ed2f4607a8bffe3c5deb6b1b955a585
