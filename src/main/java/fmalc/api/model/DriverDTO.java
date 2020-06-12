package fmalc.api.model;
import lombok.Data;

@Data
public class DriverDTO {
    private Integer id;
    private Integer status;
    private String driverStatus;
    private DriverLicenseDTO licenseDTO;
    private AccountDTO accountDTO;
    private String identityNo;
    private String name;
    private String phoneNumber;
}
