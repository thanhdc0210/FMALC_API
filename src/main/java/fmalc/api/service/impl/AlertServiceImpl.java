package fmalc.api.service.impl;

import fmalc.api.entity.Alert;
import fmalc.api.repository.AlertRepository;
import fmalc.api.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    AlertRepository alertRepository;

    @Override
    public List<Alert> getAlerts() {
        return alertRepository.findAllByOrderByIdDesc();
    }
}
