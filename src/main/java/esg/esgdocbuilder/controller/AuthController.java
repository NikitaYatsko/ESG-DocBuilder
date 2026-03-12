package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.request.LoginRequest;
import esg.esgdocbuilder.model.dto.response.UserAuthResponse;
import esg.esgdocbuilder.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
