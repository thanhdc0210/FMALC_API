package fmalc.api.service;

import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.dto.ConsignmentUpdateDTO;
import fmalc.api.entity.Consignment;

import java.text.ParseException;
import java.util.List;

public interface ConsignmentService {

//    List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(List<Integer> status, String username);

    Consignment findById(int consignment_id);

    Consignment save(ConsignmentRequestDTO consignmentRequestDTO) throws ParseException;

    List<Consignment> findAll();

    List<Consignment> getAllByStatus(Integer status);

    Consignment consignmentConfirm(ConsignmentRequestDTO consignmentRequestDTO);

    List<Consignment> findConsignmentByVehicle(int idVehicle);

    int updateStatus(int status, int id);

    List<Consignment> getConsignmentOfDriver(int id);

    int updateConsignment(ConsignmentUpdateDTO consignmentUpdateDTO);
//    Consignment mapDriverForConsignment(Consignment consignment);
}
