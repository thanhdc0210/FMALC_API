package fmalc.api.service;

import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.entity.Consignment;

import java.util.List;

public interface ConsignmentService {

    List<Consignment> findByConsignmentStatusAndUsernameForDriver(List<Integer> status, String username);

    List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(List<Integer> status, String username);

    Consignment findById(int consignment_id);

    Consignment save(ConsignmentRequestDTO consignmentRequestDTO);

    List<Consignment> findAll();

    List<Consignment> getAllByStatus(Integer status);

//    Consignment mapDriverForConsignment(Consignment consignment);
}
