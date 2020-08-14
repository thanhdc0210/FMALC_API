package fmalc.api.service;

import fmalc.api.dto.AlertRequestDTO;
import fmalc.api.entity.Alert;

import java.util.List;

public interface AlertService {
    List<Alert> getAlerts();
    Alert driverSendAlert(AlertRequestDTO alertRequestDTO);
}
