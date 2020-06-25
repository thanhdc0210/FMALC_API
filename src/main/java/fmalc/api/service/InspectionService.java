package fmalc.api.service;

import fmalc.api.entity.Inspection;

import java.util.List;

public interface InspectionService {
    List<Inspection> findAll();
}
