package fmalc.api.service;

import fmalc.api.dto.AlertRequestDTO;
import fmalc.api.dto.Paging;
import fmalc.api.entity.Alert;

import java.util.List;

public interface AlertService {
    Paging getAlerts(String username, int page);
    Alert driverSendAlert(AlertRequestDTO alertRequestDTO);
    Alert updateStatus(int id);
}
