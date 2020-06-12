package fmalc.api.service;

import fmalc.api.entities.Consignment;

import java.util.List;

public interface ConsignmentService {

    List<Consignment> findByStatus(Integer status);

    List<Consignment> findByConsignmentId(Integer consignment_id);
}
