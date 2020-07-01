package fmalc.api.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class DriverRequestDTO {
    private Integer status;
    private Integer driverLicense;
    private String identityNo;
    private String name;
    private String phoneNumber;
    private Integer fleetManagerId;
    private String no;
    private Date license_expires;
    private Date dateOfBirth;
}

