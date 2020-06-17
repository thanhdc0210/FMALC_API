package fmalc.api.service.impl;

import fmalc.api.dto.StatusRequestDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.repository.ConsignmentRepository;
import fmalc.api.service.ConsignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Override
    public List<Consignment> findByConsignmentStatusAndUsernameForDriver(StatusRequestDTO statusRequestDTO){

        return consignmentRepository.findByConsignmentStatusAndUsernameForDriver(statusRequestDTO.getStatus(), statusRequestDTO.getUsername());
    }

    @Override
    public List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(StatusRequestDTO statusRequestDTO) {
        return consignmentRepository.findByConsignmentStatusAndUsernameForFleetManager(statusRequestDTO.getStatus(), statusRequestDTO.getUsername());
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
