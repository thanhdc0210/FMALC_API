package fmalc.api.service.impl;

import fmalc.api.entity.FleetManager;
import fmalc.api.repository.FleetManagerRepository;
import fmalc.api.service.FleetManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;




@Service
public class FleetManagerServiceImpl implements FleetManagerService {

    @Autowired
    FleetManagerRepository fleetManagerRepository;

    @Override
    public List<FleetManager> getAllFleet() {
        return fleetManagerRepository.findAll();
    }
}
