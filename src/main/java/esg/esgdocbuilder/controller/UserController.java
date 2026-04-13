package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.request.UpdateRoleRequest;
import esg.esgdocbuilder.model.dto.request.UserRegistrationRequest;
import esg.esgdocbuilder.model.dto.response.UserProfileResponse;
import esg.esgdocbuilder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserProfileResponse> createNewUser(@RequestBody @Valid UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.createNewUser(request));
    }

    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<UserProfileResponse> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userid}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userid, @RequestBody UpdateRoleRequest request) {
        userService.updateUserRole(userid, request);
        return ResponseEntity.noContent().build();
    }
}
