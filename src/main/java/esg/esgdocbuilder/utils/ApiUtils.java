package esg.esgdocbuilder.utils;

import java.util.UUID;

public class ApiUtils {
    public static String generateUuidWithoutDash() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
