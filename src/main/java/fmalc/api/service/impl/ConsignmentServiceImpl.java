package fmalc.api.service.impl;

import fmalc.api.entity.Consignment;
import fmalc.api.repository.ConsignmentRepository;
import fmalc.api.service.ConsignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
