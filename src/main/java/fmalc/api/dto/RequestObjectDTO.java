package fmalc.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestObjectDTO {
    private List<ObejctScheDTO> schedule;
    private ConsignmentRequestDTO consignmentRequest;
    private ConsignmentResponseDTO newConsignment;
    private  int driver_sub;
}
