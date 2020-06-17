package fmalc.api.model;

import lombok.Data;
import java.util.Date;

@Data
public class DriverLicenseDTO {
    private Integer id;
    private Date expires;
    private String licenseType;
    private String no;
}
