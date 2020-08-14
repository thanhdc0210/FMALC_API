package fmalc.api.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class FleetManagerRequestDTO {
    private String identityNo;
    private String name;
    private String phoneNumber;
    private Date dateOfBirth;
}
