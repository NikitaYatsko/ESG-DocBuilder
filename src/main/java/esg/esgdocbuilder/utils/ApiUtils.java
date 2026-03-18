package esg.esgdocbuilder.utils;


import jakarta.servlet.http.Cookie;

import java.util.UUID;

public class ApiUtils {
    public static String generateUuidWithoutDash() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Cookie createCookie(String value) {
        Cookie refreshCookie = new Cookie("refreshToken", value);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/auth/refresh");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        return refreshCookie;
    }
}
