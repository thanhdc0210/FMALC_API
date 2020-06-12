package fmalc.api.service.impl;

import fmalc.api.entities.Consignment;
import fmalc.api.repository.ConsignmentRepository;
import fmalc.api.response.ConsignmentResponse;
import fmalc.api.service.ConsignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Override
    public List<Consignment> findByStatus(Integer status){
        return consignmentRepository.findByStatus(status);
    }

    @Override
    public List<Consignment> findByConsignmentId(Integer consignment_id) {
        return consignmentRepository.findByConsignmentId(consignment_id);
    }
}
