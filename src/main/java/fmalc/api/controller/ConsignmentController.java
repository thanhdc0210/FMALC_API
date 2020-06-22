package fmalc.api.controller;

import fmalc.api.dto.ConsignmentRequestDTO;
import fmalc.api.dto.ConsignmentResponseDTO;
import fmalc.api.dto.DetailedConsignmentDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.dto.ConsignmentDTO;
import fmalc.api.service.ConsignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/consignments")

public class ConsignmentController {

    @Autowired
    ConsignmentService consignmentService;


    @GetMapping(value = "driver")
    public ResponseEntity<List<ConsignmentDTO>> findByConsignmentStatusAndUsernameForDriver(@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username){
        List<Consignment> consignments = consignmentService.findByConsignmentStatusAndUsernameForDriver(status, username);

        if (consignments == null){
            return ResponseEntity.noContent().build();
        }
        List<ConsignmentDTO> consignmentResponses = new ArrayList<>(new ConsignmentDTO().mapToListResponse(consignments));

        return ResponseEntity.ok().body(consignmentResponses);
    }

    @GetMapping(value = "fleetManager")
    public ResponseEntity<List<ConsignmentDTO>> findByConsignmentStatusAndUsernameForFleetManager(@RequestParam(value = "status") List<Integer> status, @RequestParam(value = "username") String username){
        List<Consignment> consignments = consignmentService.findByConsignmentStatusAndUsernameForFleetManager(status, username);

        if (consignments == null){
            return ResponseEntity.noContent().build();
        }
        List<ConsignmentDTO> consignmentResponses = new ArrayList<>(new ConsignmentDTO().mapToListResponse(consignments));
        System.out.println(consignmentResponses.size());
        return ResponseEntity.ok().body(consignmentResponses);
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<DetailedConsignmentDTO> findById(@PathVariable("id") Integer id){
        Consignment consignment = consignmentService.findById(id);
        if (consignment == null || consignment.equals("")){
            return ResponseEntity.noContent().build();
        }
        DetailedConsignmentDTO detailedConsignmentDTO = new DetailedConsignmentDTO(consignment);

        return ResponseEntity.ok().body(detailedConsignmentDTO);
    }
//
//    @GetMapping(value = "/all")
//    public ResponseEntity<List<Consignment>> findAll(){
//
//        return ResponseEntity.ok().body(consignmentService.findAll());
//    }

    @GetMapping
    public ResponseEntity<List<ConsignmentResponseDTO>> getAll() {
        List<Consignment> consignments = consignmentService.findAll();
        if (consignments.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(new ConsignmentResponseDTO().mapToListResponse(consignments));
    }

    @GetMapping(value = "status")
    public ResponseEntity<List<ConsignmentResponseDTO>> getAllByStatus(@RequestParam("status") Integer status) {
        List<Consignment> consignments = consignmentService.getAllByStatus(status);
        if (consignments.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(new ConsignmentResponseDTO().mapToListResponse(consignments));
    }

    @PostMapping
    public ResponseEntity<ConsignmentResponseDTO> createConsignment(@RequestBody ConsignmentRequestDTO consignmentRequestDTO){
        try {
            Consignment consignment = consignmentService.save(consignmentRequestDTO);
            return ResponseEntity.ok().body(new ConsignmentResponseDTO().mapToResponse(consignment));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
