package fmalc.api.service;

import fmalc.api.dto.StatusRequestDTO;
import fmalc.api.entity.Consignment;

import java.util.List;
import java.util.Map;

public interface ConsignmentService {

    List<Consignment> findByConsignmentStatusAndUsernameForDriver(StatusRequestDTO statusRequestDTO);

    List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(StatusRequestDTO statusRequestDTO);

    Consignment findById(Integer consignment_id);

    List<Consignment> findAll();
}
