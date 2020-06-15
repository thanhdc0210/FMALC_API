package fmalc.api.dto;

import lombok.Data;

@Data
public class DriverLicenseResponseDTO {
    private Integer id;
    private String licenseType;
    private String no;
}
