package fmalc.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DriverForDetailDTO {
    private Integer id;
    private Integer status;
    private Integer driverLicense;
    private String identityNo;
    private String name;
    private String phoneNumber;
    private Date license_expires;
    private String no;
    private Date dateOfBirth;
    private Float workingHour;
    private String image;
    private FLeetManagerResponseDTO fleetManager;
}
