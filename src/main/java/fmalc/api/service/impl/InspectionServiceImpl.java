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
        return inspectionRepository.findAllByOrderByIdAsc();
    }

    @Override
    public Inspection findById(Integer id) {
        if (!inspectionRepository.existsById(id)) {
            return null;
        }
        return inspectionRepository.findById(id).get();
    }

    @Override
    public Inspection save(Inspection inspection) {
        inspection.setId(null);
        return inspectionRepository.save(inspection);
    }

    @Override
    public Inspection update(Integer id, Inspection inspection) throws Exception {
        if (!inspectionRepository.existsById(id)) {
            throw new Exception();
        }
        inspectionRepository.update(id, inspection.getInspectionName());
        return inspectionRepository.findById(id).get();
    }
}
