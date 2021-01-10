package ua.ies.g23.Covinfo19;

public class SecurityConstants {
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String PUBLIC_URL = "/api/v1/public";
    public static final String PRIVATE_URL = "/api/v1/private";
}
