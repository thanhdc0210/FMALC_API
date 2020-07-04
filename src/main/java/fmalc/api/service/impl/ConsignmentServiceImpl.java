package fmalc.api.service.impl;

import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.repository.ConsignmentRepository;
import fmalc.api.service.ConsignmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    private ConsignmentRepository consignmentRepository;

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
