package fmalc.api.common;

/**
 * @author Bao Giang Trinh Le
 */
public enum ResponseStatusEnum {
    SUCCESS(200), //OK
    CREATED( 201),
    NO_CONTENT(204),
    PARAMS_INVALID(400), //BAD REQUEST
    NOT_FOUND(404),
    TOKEN_EXPIRED(410),
    UNAUTHORIZED(401),
    REQUEST_TIMEOUT(408),
    DEVICE_NOT_FOUND(412),
    FAIL(500);//Internal Server Error

    private final int value;
    private ResponseStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
