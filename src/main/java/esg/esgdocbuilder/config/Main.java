package esg.esgdocbuilder.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Main {
    public static void main(String[] args) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "dimafrontend";
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(encodedPassword);
    }
}
