package esg.esgdocbuilder.utils;


import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;

import java.util.UUID;

public class ApiUtils {
    public static String generateUuidWithoutDash() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static ResponseCookie createRefreshTokenCookie(String value) {
        return ResponseCookie.from("refreshToken", value)
                .httpOnly(true)
                .secure(true) // Важно для SameSite=None
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None") // Критически важно!
                .build();
    }
}
