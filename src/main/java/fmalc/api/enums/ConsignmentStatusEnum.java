package fmalc.api.enums;

public enum ConsignmentStatusEnum {
    ĐANG_CHỜ_XỬ_LÝ("Đang chờ xử lý"){@Override public int getValue(){return 0;}},
    ĐANG_LẤY_HÀNG("Đang lấy hàng"){@Override public int getValue(){return 1;}},
    ĐANG_GIAO_HÀNG("Đang_giao_hàng"){@Override public int getValue(){return 2;}},
    HOÀN_THÀNH("Hoàn thành"){@Override public int getValue(){return 3;}},
    BỊ_HỦY("Bị hủy"){@Override public int getValue(){return 4;}},
    THIẾU_CHỨNG_TỪ("Thiếu chứng từ"){@Override public int getValue(){return 5;}},
    TẠM_HOÃN("Tạm hoãn"){@Override public int getValue(){return 6;}},
    ĐANG_KHỞI_HÀNH("Đang khởi hành"){@Override public int getValue(){return 7;}};

    String consignmentStatusEnum;

    ConsignmentStatusEnum(String consignmentStatusEnum){
        this.consignmentStatusEnum = consignmentStatusEnum;
    }

    public String getConsignmentStatusEnum() {
        return consignmentStatusEnum;
    }

    public String getValueEnumToShow(int status){
        switch (status){
            case 0:
                return ĐANG_CHỜ_XỬ_LÝ.getConsignmentStatusEnum();
            case 1:
                return ĐANG_LẤY_HÀNG.getConsignmentStatusEnum();
            case 2:
                return ĐANG_GIAO_HÀNG.getConsignmentStatusEnum();
            case 3:
                return HOÀN_THÀNH.getConsignmentStatusEnum();
            case 4:
                return BỊ_HỦY.getConsignmentStatusEnum();
            case 5:
                return THIẾU_CHỨNG_TỪ.getConsignmentStatusEnum();
            case 6:
                return TẠM_HOÃN.getConsignmentStatusEnum();
            case 7:
                return ĐANG_KHỞI_HÀNH.getConsignmentStatusEnum();
            default:
                throw new AssertionError("Unknown operations " + this);
        }
    }

    public abstract int getValue();
}
