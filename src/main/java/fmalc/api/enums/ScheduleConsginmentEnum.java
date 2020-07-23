package fmalc.api.enums;

public enum  ScheduleConsginmentEnum {
    SCHEDULE_CHECK("check"){@Override public int getValue(){return 0;}},
    SCHEDULE_NOT_CHECK("not check"){@Override public int getValue(){return 1;}};

    String scheduleConsginmentEnum;

    ScheduleConsginmentEnum(String scheduleConsginmentEnum) {
        this.scheduleConsginmentEnum = scheduleConsginmentEnum;
    }

    public String getScheduleConsginmentEnum() {
        return scheduleConsginmentEnum;
    }

    public abstract int getValue();

    public static String getValueEnumToShow(Integer type){
        switch (type){
            case 0:
                return SCHEDULE_CHECK.getScheduleConsginmentEnum();
            case 1:
                return SCHEDULE_NOT_CHECK.getScheduleConsginmentEnum();
            default:
                throw new AssertionError("Unknown operations");
        }
    }
}
