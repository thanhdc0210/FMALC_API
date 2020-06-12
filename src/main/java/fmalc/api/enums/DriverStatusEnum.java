package fmalc.api.enums;

public enum DriverStatusEnum {
    ĐANG_RẢNH("Đang rảnh"){@Override public int getValue(){return 0;}},
    ĐANG_CHẠY("Đang chạy"){@Override public int getValue(){return 1;}},
    XIN_NGHỈ_PHÉP("Xin nghỉ phép"){@Override public int getValue(){return 2;}},
    CÓ_LỊCH_CHẠY("Có lịch chạy"){@Override public int getValue(){return 3;}}
    ;

    String driverStatusEnum;

    DriverStatusEnum(String driver_status_enum) {
        this.driverStatusEnum = driver_status_enum;
    }

    public String getDriverStatusEnum(){
        return driverStatusEnum;
    }

    public abstract int getValue();

    public String getValueEnumToShow(int status){
        switch (status){
            case 0:
                return ĐANG_RẢNH.getDriverStatusEnum();
            case 1:
                return ĐANG_CHẠY.getDriverStatusEnum();
            case 2:
                return XIN_NGHỈ_PHÉP.getDriverStatusEnum();
            case 3:
                return CÓ_LỊCH_CHẠY.getDriverStatusEnum();
            default:
                throw new AssertionError("Unknown operations " + this);
        }
    }
}
