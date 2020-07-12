package fmalc.api.enums;

public enum NotificationTypeEnum {
    ODD_HOURS_ALERTS("Chạy xe ngoài giờ làm việc"){@Override public int getValue(){return 0;}},
    LONG_IDLE_TIMES("Dừng xe quá lâu"){@Override public int getValue(){return 1;}};

    String notificationTypeEnum;

    NotificationTypeEnum(String notificationTypeEnum) {
        this.notificationTypeEnum = notificationTypeEnum;
    }

    public String getNotificationTypeEnum() {
        return notificationTypeEnum;
    }

    public abstract int getValue();

    public static String getValueEnumToShow(Integer type){
        switch (type){
            case 0:
                return ODD_HOURS_ALERTS.getNotificationTypeEnum();
            case 1:
                return LONG_IDLE_TIMES.getNotificationTypeEnum();
            default:
                throw new AssertionError("Unknown operations");
        }
    }
}
