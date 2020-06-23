package fmalc.api.enums;

public enum TypeLocationEnum {
    DELIVERED_PLACE("Lấy hàng"){@Override public int getValue(){return 0;}},
    RECEIVED_PLACE("Giao hàng"){@Override public int getValue(){return 1;}};

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
            case 0:
                return DELIVERED_PLACE.getTypeLocationEnum();
            case 1:
                return RECEIVED_PLACE.getTypeLocationEnum();
            default:
                throw new AssertionError("Unknown operations");
        }
    }
}
