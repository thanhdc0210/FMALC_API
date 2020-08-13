package fmalc.api.service.impl;

import fmalc.api.entity.FuelType;
import fmalc.api.repository.FuelTypeRepository;
import fmalc.api.service.FuelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FuelTypeServiceImpl implements FuelTypeService {

    @Autowired
    FuelTypeRepository fuelTypeRepository;

    @Override
    public List<FuelType> getListFuelType() {
        return fuelTypeRepository.findAll();
    }

    @Override
    public void createOrUpdateFuelType(List<Double> prices) {
        List<String> type = Arrays.asList("Xăng RON 95-IV", "Xăng RON 95-III", "E5 RON 92-II", "DO 0,001S-V", "DO 0,05S-II");
        for (int i = 0; i < 5; i++) {
            FuelType fuelType = fuelTypeRepository.findByType(type.get(i));
            if (fuelType == null) {
                fuelType = new FuelType();
                fuelType.setCurrentPrice(prices.get(i));
                fuelType.setType(type.get(i));
                fuelTypeRepository.save(fuelType);
            } else {
                fuelTypeRepository.updatePrice(fuelType.getId(), prices.get(i));
            }
        }
    }
}
