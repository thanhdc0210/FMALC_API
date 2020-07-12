package fmalc.api.enums;

public enum ReportIssueTypeEnum {
    BEFORE_DELIVERY("Trước khi chạy"){@Override public int getValue(){return 0;}},
    AFTER_DELIVERY("Sau khi chạy"){@Override public int getValue(){return 1;}};

    String reportIssueTypeEnum;

    ReportIssueTypeEnum(String reportIssueTypeEnum) {
        this.reportIssueTypeEnum = reportIssueTypeEnum;
    }

    public String getReportIssueTypeEnum() {
        return reportIssueTypeEnum;
    }

    public abstract int getValue();

    public static String getValueEnumToShow(Integer type){
        switch (type){
            case 0:
                return BEFORE_DELIVERY.getReportIssueTypeEnum();
            case 1:
                return AFTER_DELIVERY.getReportIssueTypeEnum();
            default:
                throw new AssertionError("Unknown operations");
        }
    }
}
