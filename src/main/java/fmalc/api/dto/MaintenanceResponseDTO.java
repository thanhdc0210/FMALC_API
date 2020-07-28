package fmalc.api.dto;

import lombok.Data;
import fmalc.api.entity.Maintenance;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class MaintenanceResponseDTO implements Serializable {
    private Integer maintainTypeId;
    private Integer maintainId;
    private String content;
    private String maintainTypeName;
    private String licensePlates;
    private Date maintainDate;

    public MaintenanceResponseDTO(Maintenance dto ){
        this.maintainId = dto.getId();
        this.maintainTypeId = dto.getMaintenanceType().getId();
        this.content = dto.getMaintenanceType().getContent();
        this.maintainTypeName = dto.getMaintenanceType().getMaintainTypeName();
        this.licensePlates = dto.getVehicle().getLicensePlates();
        this.maintainDate = dto.getPlannedMaintainDate();
    }

    public List<MaintenanceResponseDTO> mapToListResponse(List<Maintenance> baseEntities){
        return baseEntities.stream().map(MaintenanceResponseDTO::new).collect(Collectors.toList());
    }
}
