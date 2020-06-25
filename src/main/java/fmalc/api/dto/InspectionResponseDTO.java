package fmalc.api.dto;

import fmalc.api.entity.Inspection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InspectionResponseDTO {
    List<String> vehicleLicensePlates;
    List<Inspection> inspections;
}
