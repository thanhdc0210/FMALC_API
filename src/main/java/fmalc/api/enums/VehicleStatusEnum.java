package fmalc.api.enums;

public enum VehicleStatusEnum {
    ĐANG_RẢNH("Đang rảnh"){@Override public int getValue(){return 0;}}, // Xe chưa xếp lịch chạy
    ĐANG_BẢO_TRÌ("Đang bảo trì"){@Override public int getValue(){return 1;}},
    ĐANG_CHẠY("Đang chạy"){@Override public int getValue(){return 2;}},
    ĐÃ_BÁN("Đã bán"){@Override public int getValue(){return 3;}},
    CÓ_LỊCH_CHẠY("Có lịch chạy"){@Override public  int getValue(){return 4;}}; // Xe đã xếp lịch nhưng chưa chạy

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
            case 4:
                return CÓ_LỊCH_CHẠY.getVehicleStatusEnum();
            default:throw new AssertionError("Unknown operations " + this);
        }
    }

}
