package fmalc.api.controller;

import fmalc.api.entities.Consignment;
import fmalc.api.dto.ConsignmentDTO;
import fmalc.api.dto.DetailedConsignmentDTO;
import fmalc.api.service.ConsignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/consignments")

public class ConsignmentController {

    @Autowired
    ConsignmentService consignmentService;


    @GetMapping(value = "status/{status}")

    public ResponseEntity<List<ConsignmentDTO>> findByStatus(@PathVariable("status") Integer status){
        List<Consignment> consignments = consignmentService.findByStatus(status);
        if (consignments.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<ConsignmentDTO> consignmentResponses = new ArrayList<>(new ConsignmentDTO().mapToListResponse(consignments));

        return ResponseEntity.ok().body(consignmentResponses);
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<List<DetailedConsignmentDTO>> findByConsignmentId(@PathVariable("id") Integer id){
        List<Consignment> consignments = consignmentService.findByConsignmentId(id);
        if (consignments.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<DetailedConsignmentDTO> detailedConsignmentResponses = new ArrayList<>(new DetailedConsignmentDTO().mapToListResponse(consignments));

        return ResponseEntity.ok().body(detailedConsignmentResponses);
    }
}
