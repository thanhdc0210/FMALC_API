package fmalc.api.dto;

import fmalc.api.entity.Maintenance;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MaintainanceResponse {
    private int id;
    private Date actualMaintainDate;
    private int kmOld;
    private int status;
    private String licensePlates;
    private String nameDriver;
    private String phoneNumberDriver;
    private String content;

    public MaintainanceResponse mapToResponse(Maintenance maintenance) {
        ModelMapper modelMapper = new ModelMapper();
        MaintainanceResponse maintainanceResponse = modelMapper.map(maintenance, MaintainanceResponse.class);
        maintainanceResponse.setLicensePlates(maintenance.getVehicle().getLicensePlates());
        if (maintenance.getDriver() != null) {
            maintainanceResponse.setNameDriver(maintenance.getDriver().getName());
            maintainanceResponse.setPhoneNumberDriver(maintenance.getDriver().getPhoneNumber());
        }
        maintainanceResponse.setContent(maintenance.getMaintainType().getContent());
        int status = 1;
        LocalDate today = LocalDate.now();
        if (maintenance.getActualMaintainDate().toLocalDate().isAfter(today)) {
            status = 0;
        } else if (maintenance.getActualMaintainDate().toLocalDate().isBefore(today)) {
            status = 2;
        }
        maintainanceResponse.setStatus(status);
        return maintainanceResponse;
    }

    public List<MaintainanceResponse> mapToListResponse(List<Maintenance> maintenances) {
        List<MaintainanceResponse> result = maintenances
                .stream()
                .map(maintenance -> mapToResponse(maintenance))
                .collect(Collectors.toList());
        return result;
    }
}
