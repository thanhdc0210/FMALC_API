package fmalc.api.enums;

public enum TypeLocationEnum {
    DELIVERED_PLACE("Nơi lấy hàng"){@Override public int getValue(){return 0;}},
    RECEIVED_PLACE("Nơi giao hàng"){@Override public int getValue(){return 1;}};

    String typeLocationEnum;

    TypeLocationEnum(String typeLocationEnum) {
        this.typeLocationEnum = typeLocationEnum;
    }

    public String getTypeLocationEnum() {
        return typeLocationEnum;
    }

    public abstract int getValue();

    public static String getValueEnumToShow(Integer type){
        switch (type){
            case 1:
                return DELIVERED_PLACE.getTypeLocationEnum();
            case 2:
                return RECEIVED_PLACE.getTypeLocationEnum();
            default:
                throw new AssertionError("Unknown operations");
        }
    }
}
