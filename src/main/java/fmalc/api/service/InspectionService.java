package fmalc.api.service;

import fmalc.api.entity.Inspection;

import java.util.List;

public interface InspectionService {
    List<Inspection> findAll();

    Inspection findById(Integer id);

    Inspection save(Inspection inspection);

    Inspection update(Inspection inspection) throws Exception;

    List<Inspection> findAllOrderById();
}
