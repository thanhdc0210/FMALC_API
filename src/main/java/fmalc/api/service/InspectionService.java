package fmalc.api.service;

import fmalc.api.entity.Inspection;

import java.util.List;

public interface InspectionService {
    List<Inspection> findAll();

    Inspection findById(Integer id);

    Inspection save(Inspection inspection);

    Inspection update(Integer id, Inspection inspection) throws Exception;
}
