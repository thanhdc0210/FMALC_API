package fmalc.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestSaveScheObjDTO {
    List<ObejctScheDTO> obejctScheDTOS;
    ConsignmentRequestDTO consignmentRequestDTO;
}
