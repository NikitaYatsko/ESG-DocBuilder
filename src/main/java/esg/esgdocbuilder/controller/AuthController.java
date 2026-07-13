package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.request.ForgotPasswordRequest;
import esg.esgdocbuilder.model.dto.request.LoginRequest;
import esg.esgdocbuilder.model.dto.request.ResetPasswordRequest;
import esg.esgdocbuilder.model.dto.response.UserAuthResponse;
import esg.esgdocbuilder.service.AuthService;
import esg.esgdocbuilder.service.PasswordResetTokenService;
import esg.esgdocbuilder.service.RefreshTokenService;
import esg.esgdocbuilder.utils.ApiUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {

        UserAuthResponse auth = authService.login(request);

        ResponseCookie refreshCookie = ApiUtils.createRefreshTokenCookie(auth.getRefreshToken());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok(auth);
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserAuthResponse> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }
        UserAuthResponse auth = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletResponse response,
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {

        if (refreshToken != null) {
            refreshTokenService.deleteRefreshToken(refreshToken);
        }

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
        log.info("Creating password reset Token");
        passwordResetTokenService.createPasswordResetToken(forgotPasswordRequest);
        return ResponseEntity.ok("Password reset link has been sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        log.info("Getting password reset request");
        passwordResetTokenService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Password has been changed successfully");
    }


}
