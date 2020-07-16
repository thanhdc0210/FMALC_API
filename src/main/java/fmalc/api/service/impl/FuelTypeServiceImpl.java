package fmalc.api.service.impl;

import fmalc.api.entity.FuelType;
import fmalc.api.repository.FuelTypeRepository;
import fmalc.api.service.FuelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FuelTypeServiceImpl implements FuelTypeService {

    @Autowired
    FuelTypeRepository fuelTypeRepository;

    @Override
    public List<FuelType> getListFuelType() {
        List<FuelType> fuelTypeList = new ArrayList<>();
        fuelTypeList = fuelTypeRepository.findAll();
        return fuelTypeList;
    }
}
