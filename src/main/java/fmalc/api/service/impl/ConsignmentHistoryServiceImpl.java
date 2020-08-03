package fmalc.api.service.impl;

import fmalc.api.entity.Consignment;
import fmalc.api.entity.ConsignmentHistory;
import fmalc.api.entity.FleetManager;
import fmalc.api.repository.ConsignmentHistoryRepository;
import fmalc.api.service.ConsignmentHistoryService;
import fmalc.api.service.ConsignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class ConsignmentHistoryServiceImpl implements ConsignmentHistoryService {

    @Autowired
    ConsignmentService consignmentService;
    @Autowired
    ConsignmentHistoryRepository consignmentHistoryRepository;

    @Override
    public ConsignmentHistory save(int idConsignment, FleetManager fleetManager,String note) {
        Consignment consignment = consignmentService.findById(idConsignment);
        ConsignmentHistory consignmentHistory = new ConsignmentHistory();
        consignmentHistory.setConsignment(consignment);
        consignmentHistory.setFleetManager(fleetManager);
        consignmentHistory.setNote(note );
        consignmentHistory.setTime(new Date(new java.util.Date().getTime()));
        consignmentHistory = consignmentHistoryRepository.save(consignmentHistory);
        return consignmentHistory;
    }
}
