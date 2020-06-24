package fmalc.api.enums;

public enum DriverLicenseEnum {
    B2("B2"){@Override public int getValue(){return 0;}},
    C("C"){@Override public int getValue(){return 1;}},
    D("D"){@Override public int getValue(){return 2;}},
    E("E"){@Override public int getValue(){return 3;}};

    String driverLicenseEnum;

    DriverLicenseEnum(String consignmentStatusEnum){
        this.driverLicenseEnum = consignmentStatusEnum;
    }

    public String getDriverLicenseEnum() {
        return driverLicenseEnum;
    }

    public static String getValueEnumToShow(int status){
        switch (status){
            case 0:
                return B2.getDriverLicenseEnum();
            case 1:
                return C.getDriverLicenseEnum();
            case 2:
                return D.getDriverLicenseEnum();
            case 3:
                return E.getDriverLicenseEnum();
            default:
                throw new AssertionError("Unknown operations ");
        }
    }

    public abstract int getValue();
}
