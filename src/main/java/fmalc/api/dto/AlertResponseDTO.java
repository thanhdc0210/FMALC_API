package fmalc.api.dto;

import fmalc.api.entity.Alert;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AlertResponseDTO {
    private Integer id;
    private VehicleResponseDTO vehicle;
    private DriverResponseDTO driver;
    private Timestamp time;
    private Integer level;
    private String content;
    private Boolean status;

    public List<AlertResponseDTO> mapToListResponse(List<Alert> alerts) {
        return alerts
                .stream()
                .map(alert -> mapToResponse(alert))
                .collect(Collectors.toList());
    }

    private AlertResponseDTO mapToResponse(Alert alert) {
        ModelMapper modelMapper = new ModelMapper();
        AlertResponseDTO alertResponseDTO = modelMapper.map(alert, AlertResponseDTO.class);
        return alertResponseDTO;
    }
}
