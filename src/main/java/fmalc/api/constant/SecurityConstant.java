package fmalc.api.constant;

public class SecurityConstant {
    public static final String SECRET = "secretKey";
    public static final long EXPIRATION_TIME = 60 * 60 * 24 * 365 * 60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTH_LOGIN_URL = "/api/v1.0/login";
}
