package fmalc.api.controller;

import fmalc.api.dto.StatusRequestDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.dto.ConsignmentDTO;
import fmalc.api.dto.DetailedConsignmentDTO;
import fmalc.api.service.ConsignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1.0/consignments")

public class ConsignmentController {

    @Autowired
    ConsignmentService consignmentService;


    @GetMapping(value = "driver")
    public ResponseEntity<List<ConsignmentDTO>> findByConsignmentStatusAndUsernameForDriver(@RequestBody StatusRequestDTO statusRequestDTO){
        List<Consignment> consignments = consignmentService.findByConsignmentStatusAndUsernameForDriver(statusRequestDTO);

        if (consignments == null){
            return ResponseEntity.noContent().build();
        }
        List<ConsignmentDTO> consignmentResponses = new ArrayList<>(new ConsignmentDTO().mapToListResponse(consignments));

        return ResponseEntity.ok().body(consignmentResponses);
    }

    @GetMapping(value = "fleetManager")
    public ResponseEntity<List<ConsignmentDTO>> findByConsignmentStatusAndUsernameForFleetManager(@RequestBody StatusRequestDTO statusRequestDTO){
        List<Consignment> consignments = consignmentService.findByConsignmentStatusAndUsernameForFleetManager(statusRequestDTO);

        if (consignments == null){
            return ResponseEntity.noContent().build();
        }
        List<ConsignmentDTO> consignmentResponses = new ArrayList<>(new ConsignmentDTO().mapToListResponse(consignments));

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

    @GetMapping(value = "/all")
    public ResponseEntity<List<Consignment>> findAll(){

        return ResponseEntity.ok().body(consignmentService.findAll());
    }

}
