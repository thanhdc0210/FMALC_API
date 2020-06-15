package fmalc.api.service;

import fmalc.api.entity.Consignment;

import java.util.List;

public interface ConsignmentService {

    List<Consignment> findByStatus(Integer status);

    Consignment findById(Integer consignment_id);

    List<Consignment> findAll();
}
