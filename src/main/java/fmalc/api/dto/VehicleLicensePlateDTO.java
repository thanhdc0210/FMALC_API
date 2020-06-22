package fmalc.api.dto;

import fmalc.api.entity.Consignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class VehicleLicensePlateDTO {
    private String driverLicensePlate;

    public VehicleLicensePlateDTO(Consignment consignment) {
        driverLicensePlate = consignment.getSchedule().getVehicle().getLicensePlates();
    }

    public List<VehicleLicensePlateDTO> mapToListResponse(List<Consignment> baseEntities){
        return baseEntities.stream().map(VehicleLicensePlateDTO::new).collect(Collectors.toList());
    }
}
