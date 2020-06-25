package fmalc.api.service.impl;

import fmalc.api.entity.Inspection;
import fmalc.api.repository.InspectionRepository;
import fmalc.api.service.InspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InspectionServiceImpl implements InspectionService {

    @Autowired
    InspectionRepository inspectionRepository;

    @Override
    public List<Inspection> findAll() {
        return inspectionRepository.findAll();
    }
}
