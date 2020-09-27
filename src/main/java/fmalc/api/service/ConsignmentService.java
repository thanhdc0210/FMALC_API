package fmalc.api.service;

import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.dto.ConsignmentUpdateDTO;
import fmalc.api.dto.NewScheduleDTO;
import fmalc.api.dto.Paging;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.Schedule;

import java.text.ParseException;
import java.util.List;

public interface ConsignmentService {

//    List<Consignment> findByConsignmentStatusAndUsernameForFleetManager(List<Integer> status, String username);

    Consignment findById(int consignment_id);

    Consignment save(ConsignmentRequestDTO consignmentRequestDTO) throws ParseException;

    List<Consignment> findAll();

   Paging getAllByStatus(Integer status, String username, int type, int page, String search);

    Consignment consignmentConfirm(ConsignmentRequestDTO consignmentRequestDTO);

    List<Consignment> findConsignmentByVehicle(int idVehicle);

    int updateStatus(int status, int id);

    List<Consignment> getConsignmentOfDriver(int id, int status);
    List<Schedule> findScheduleByConsignment(int id);
    Consignment cancelConsignment(int id, String content, String username);
    int updateConsignment(ConsignmentUpdateDTO consignmentUpdateDTO, String username);

    NewScheduleDTO findVehicleDriver(ConsignmentRequestDTO consignmentRequestDTO);
    Integer findConsignmentId(Integer driverId, Integer vehicleId);
//    Consignment mapDriverForConsignment(Consignment consignment);
}
