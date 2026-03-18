package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.request.LoginRequest;
import esg.esgdocbuilder.model.dto.response.UserAuthResponse;
import esg.esgdocbuilder.service.AuthService;
import esg.esgdocbuilder.utils.ApiUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {

        UserAuthResponse auth = authService.login(request);

        Cookie refreshCookie = ApiUtils.createCookie(auth.getRefreshToken());
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(auth);
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserAuthResponse> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }

        UserAuthResponse auth = authService.refreshAccessToken(refreshToken);

        Cookie authCookie = ApiUtils.createCookie(auth.getRefreshToken());
        response.addCookie(authCookie);

        return ResponseEntity.ok(auth);
    }

}
