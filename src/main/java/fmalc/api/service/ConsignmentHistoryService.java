package fmalc.api.service;

import fmalc.api.entity.ConsignmentHistory;
import fmalc.api.entity.FleetManager;

public interface ConsignmentHistoryService {
    ConsignmentHistory save(int idConsignment, FleetManager fleetManager,String note);
}
