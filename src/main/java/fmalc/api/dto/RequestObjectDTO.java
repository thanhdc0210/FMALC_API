package fmalc.api.dto;

import lombok.Data;

import java.util.*;

@Data
public class RequestObjectDTO {
    private List<ObejctScheDTO> schedule;
    private ConsignmentRequestDTO consignmentRequest;
    private ConsignmentResponseDTO newConsignment;

}
