package fmalc.api.controller;

import fmalc.api.dto.FuelTypeResponseDTO;
import fmalc.api.entity.FuelType;
import fmalc.api.service.FuelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/fuel-type")
public class FuelTypeController {

    @Autowired
    FuelTypeService fuelTypeService;

    @GetMapping
    public ResponseEntity<List<FuelTypeResponseDTO>> getFuelTypes(){
        List<FuelType> fuelTypeList = fuelTypeService.getListFuelType();

        if (fuelTypeList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<FuelTypeResponseDTO> result = new ArrayList<>(new FuelTypeResponseDTO().mapToListResponse(fuelTypeList));
        return ResponseEntity.ok().body(result);

    }
}
