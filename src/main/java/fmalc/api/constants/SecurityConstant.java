package fmalc.api.constants;

public class SecurityConstant {
    public static final String SECRET = "secretKey";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTH_LOGIN_URL = "/login";
}
