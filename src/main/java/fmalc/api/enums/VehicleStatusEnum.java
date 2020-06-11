package fmalc.api.enums;

public enum VehicleStatusEnum {
    ĐANG_RẢNH("Đang rảnh"),
    ĐANG_BẢO_TRÌ("Đang bảo trì"),
    ĐANG_CHẠY("Đang chạy"),
    ĐÃ_BÁN("Đã bán");

    String vehicleStatusEnum;

    VehicleStatusEnum(String vehicleStatusEnum){
        this.vehicleStatusEnum = vehicleStatusEnum;
    }

    public String getVehicleStatusEnum() {
        return vehicleStatusEnum;
    }
}
