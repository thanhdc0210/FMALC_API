package fmalc.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DriverForScheduleDTO {
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
}
