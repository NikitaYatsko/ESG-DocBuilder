package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.request.LoginRequest;
import esg.esgdocbuilder.model.dto.response.UserAuthResponse;
import esg.esgdocbuilder.service.AuthService;
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
            @Valid @RequestBody LoginRequest request
    ) {
        UserAuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/refresh/token")
    public ResponseEntity<UserAuthResponse> refreshToken(@RequestParam String refreshToken) {
        UserAuthResponse response = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(response);
    }

}
