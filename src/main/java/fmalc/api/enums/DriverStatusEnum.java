package fmalc.api.enums;

public enum DriverStatusEnum {
    ĐANG_RẢNH("Đang rảnh"),
    ĐANG_CHẠY("Đang chạy"),
    XIN_NGHỈ_PHÉP("Xin nghỉ phép")
    ;

    String driver_status_enum;

    DriverStatusEnum(String driver_status_enum) {
        this.driver_status_enum = driver_status_enum;
    }

    public String getDriver_status_enum(){
        return driver_status_enum;
    }
}
