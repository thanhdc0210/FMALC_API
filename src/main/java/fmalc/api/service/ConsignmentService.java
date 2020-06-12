package fmalc.api.service;

import fmalc.api.entities.Consignment;
import fmalc.api.response.AccountResponse;
import fmalc.api.response.ConsignmentResponse;

import java.util.List;
import java.util.Optional;

public interface ConsignmentService {

    List<Consignment> findByStatus(Integer status);

    List<Consignment> findByConsignmentId(Integer consignment_id);
}
