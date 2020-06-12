package fmalc.api.controller;

import fmalc.api.entities.Consignment;
import fmalc.api.response.ConsignmentResponse;
import fmalc.api.response.DetailedConsignmentResponse;
import fmalc.api.service.ConsignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(name = "/api/v1.0/consigments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ConsignmentController {

    @Autowired
    ConsignmentService consignmentService;

    @GetMapping(value = "/homepage/{status}")
    public ResponseEntity<List<ConsignmentResponse>> findByStatus(@PathVariable("status") Integer status){
        List<Consignment> consignments = consignmentService.findByStatus(status);
        if (consignments.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<ConsignmentResponse> consignmentResponses = new ArrayList<>(new ConsignmentResponse().mapToListResponse(consignments));

        return ResponseEntity.ok().body(consignmentResponses);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<DetailedConsignmentResponse>> findByConsignmentId(@PathVariable("id") Integer id){
        List<Consignment> consignments = consignmentService.findByConsignmentId(id);
        if (consignments.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<DetailedConsignmentResponse> detailedConsignmentResponses = new ArrayList<>(new DetailedConsignmentResponse().mapToListResponse(consignments));

        return ResponseEntity.ok().body(detailedConsignmentResponses);
    }
}
