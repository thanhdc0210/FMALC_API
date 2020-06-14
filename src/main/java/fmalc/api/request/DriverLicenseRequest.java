package fmalc.api.request;

import lombok.Data;

import java.util.Date;

@Data
public class DriverLicenseRequest {
    private Date expires;
    private String licenseType;
    private String no;
}
