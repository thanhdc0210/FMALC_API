package fmalc.api.service.impl;

import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.repository.ConsignmentRepository;
import fmalc.api.entity.Place;
import fmalc.api.repository.PlaceRepository;
import fmalc.api.service.ConsignmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    private ConsignmentRepository consignmentRepository;

    @Autowired
    PlaceRepository placeRepository;

//    @Override
//    public List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(List<Integer> status, String username) {
//        return consignmentRepository.findByConsignmentStatusAndUsernameForFleetManager(status, username);
//    }

    @Override
    public Consignment findById(int consignment_id) {
        return consignmentRepository.findById(consignment_id);
    }

    @Override
    public Consignment save(ConsignmentRequestDTO consignmentRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Consignment consignment = modelMapper.map(consignmentRequestDTO, Consignment.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Place> place = consignmentRequestDTO.getPlace().stream()
                .map(x -> modelMapper.map(x, Place.class))
                .collect(Collectors.toList());
        consignment = consignmentRepository.save(consignment);
        List<Place> placesRecei = new ArrayList<>();
        List<Place> placesDeli = new ArrayList<>();
        List<Place> places = new ArrayList<>();
        if(consignment.getId()!=null){
            for(int i= 0; i<place.size();i++){

                if(place.get(i).getType() == TypeLocationEnum.RECEIVED_PLACE.getValue()){
                    placesRecei.add(place.get(i));
                }else if(place.get(i).getType() == TypeLocationEnum.DELIVERED_PLACE.getValue()){
                    placesDeli.add(place.get(i));
                }
//                place.get(i).setConsignment(consignment);
//                place.get(i).setPriority(1);
//                placeRepository.save(place.get(i));
            }
            String dateTemp = sdf.format(placesRecei.get(0).getPlannedTime());
            placesRecei.sort((Place s1, Place s2)-> sdf.format(s2.getPlannedTime()).compareTo(sdf.format(s1.getPlannedTime())));
            placesDeli.sort((Place s1, Place s2)-> sdf.format(s2.getPlannedTime()).compareTo(sdf.format(s1.getPlannedTime())));

           for(int i = 0 ; i< placesRecei.size() ;i++){
               placesRecei.get(i).setPriority(placesRecei.size() -i);
               placesRecei.get(i).setConsignment(consignment);
           }
            for(int i = 0 ; i< placesDeli.size();i++){
                placesDeli.get(i).setPriority(placesDeli.size()-i);
                placesDeli.get(i).setConsignment(consignment);
            }
            places.addAll(placesRecei);
            places.addAll(placesDeli);
            for(int i =0; i< places.size();i++){
                placeRepository.save(places.get(i));
            }
//            consignment.setPlaces(places);
            //            for(int i =0; i<placesRecei.size(); i++){
//               List<Place> placesReceis  = placesRecei.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
//            }
        }
        consignment.setPlaces(places);
        return consignment;
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
