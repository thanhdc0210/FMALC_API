package fmalc.api.service;

import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.entity.Consignment;

import java.util.List;

public interface ConsignmentService {

    List<Consignment> findByConsignmentStatusAndUsernameForDriver(List<Integer> status, String username);

    List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(List<Integer> status, String username);

    Consignment findById(Integer consignment_id);

    List<Consignment> findAll();

    Consignment save(ConsignmentRequestDTO consignmentRequestDTO);
}
