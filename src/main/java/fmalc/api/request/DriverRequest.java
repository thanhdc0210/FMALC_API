package fmalc.api.request;
import lombok.Data;

@Data
public class DriverRequest {
    private Integer status;
    private DriverLicenseRequest driverLicenseRequest;
    private String identityNo;
    private String name;
    private String phoneNumber;
}
