package fmalc.api.dto;

import lombok.*;

import java.util.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaintainConfirmDTO {
    private MaintainReponseDTO maintainReponseDTO;
    private List<DriverForDetailDTO> driverForDetailDTOS;
}
