package fmalc.api.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaintainConfirmDTO {
    private MaintainReponseDTO maintainReponseDTO;
    private List<DriverForDetailDTO> driverForDetailDTOS;
}
