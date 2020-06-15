package fmalc.api.service;

import fmalc.api.entity.Consignment;

import java.util.List;
import java.util.Optional;

public interface ConsignmentService {

    List<Consignment> findByStatus(Integer status);

    Optional<Consignment> findById(Integer consignment_id);
}
