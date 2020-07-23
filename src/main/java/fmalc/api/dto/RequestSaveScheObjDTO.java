package fmalc.api.dto;

import lombok.Data;

import java.util.*;

@Data
public class RequestSaveScheObjDTO {
    List<ObejctScheDTO> obejctScheDTOS;
    ConsignmentRequestDTO consignmentRequestDTO;
}
