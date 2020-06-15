package fmalc.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DriverRequestDTO {
    private Integer status;
    private DriverLicenseRequestDTO driverLicenseRequestDTO;
    private String identityNo;
    private String name;
    private String phoneNumber;
    private Date expires;
    private Integer fleetManagerId;
    private String no;
}

