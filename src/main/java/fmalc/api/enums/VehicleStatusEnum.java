package fmalc.api.enums;

public enum VehicleStatusEnum {
    ĐANG_RẢNH("Đang rảnh"){public int getValue(){return 0;}},
    ĐANG_BẢO_TRÌ("Đang bảo trì"){public int getValue(){return 1;}},
    ĐANG_CHẠY("Đang chạy"){public int getValue(){return 2;}},
    ĐÃ_BÁN("Đã bán"){public int getValue(){return 3;}};

    String vehicleStatusEnum;

    VehicleStatusEnum(String vehicleStatusEnum){
        this.vehicleStatusEnum = vehicleStatusEnum;
    }

    public String getVehicleStatusEnum() {
        return vehicleStatusEnum;
    }

    public abstract int getValue();

    public String getValueEnumToShow(int status){
        switch (status){
            case 0:
                return ĐANG_RẢNH.getVehicleStatusEnum();
            case 1:
                return ĐANG_BẢO_TRÌ.getVehicleStatusEnum();
            case 2:
                return ĐANG_CHẠY.getVehicleStatusEnum();
            case 3:
                return ĐÃ_BÁN.getVehicleStatusEnum();
            default:throw new AssertionError("Unknown operations " + this);
        }
    }

}
