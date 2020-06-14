package fmalc.api.response;

import lombok.Data;
import java.util.Date;

@Data
public class DriverLicenseResponse {
    private Integer id;
    private Date expires;
    private String licenseType;
    private String no;
}
