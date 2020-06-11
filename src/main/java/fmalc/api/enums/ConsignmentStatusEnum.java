package fmalc.api.enums;

public enum ConsignmentStatusEnum {
    ĐANG_CHỜ_XỬ_LÝ("Đang chờ xử lý"),
    ĐANG_LẤY_HÀNG("Đang lấy hàng"),
    ĐANG_GIAO_HÀNG("Đang_giao_hàng"),
    HOÀN_THÀNH("Hoàn thành"),
    BỊ_HỦY("Bị hủy"),
    THIẾU_CHỨNG_TỪ("Thiếu chứng từ"),
    TẠM_HOÃN("Tạm hoãn");

    String consignmentStatusEnum;

    ConsignmentStatusEnum(String consignmentStatusEnum){
        this.consignmentStatusEnum = consignmentStatusEnum;
    }

    public String getConsignmentStatusEnum() {
        return consignmentStatusEnum;
    }
}
