package fmalc.api.enums;

public enum DriverStatusEnum {
    ĐANG_RẢNH("Đang rảnh"){public int getValue(){return 0;}},
    ĐANG_CHẠY("Đang chạy"){public int getValue(){return 1;}},
    XIN_NGHỈ_PHÉP("Xin nghỉ phép"){public int getValue(){return 2;}}
    ;

    String driverStatusEnum;

    DriverStatusEnum(String driver_status_enum) {
        this.driverStatusEnum = driver_status_enum;
    }

    public String getDriverStatusEnum(){
        return driverStatusEnum;
    }

    public abstract int getValue();

    public static String getValueEnumToShow(int status){
        switch (status){
            case 0:
                return ĐANG_RẢNH.getDriverStatusEnum();
            case 1:
                return ĐANG_CHẠY.getDriverStatusEnum();
            case 2:
                return XIN_NGHỈ_PHÉP.getDriverStatusEnum();
            default:
                throw new AssertionError("Unknown operations");
        }
    }
}
