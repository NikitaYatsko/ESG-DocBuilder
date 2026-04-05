package esg.esgdocbuilder.controller;

import esg.esgdocbuilder.model.dto.request.UpdateUserData;
import esg.esgdocbuilder.model.dto.response.UserProfileResponse;
import esg.esgdocbuilder.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileResponse> getUserProfile() {
        return ResponseEntity.ok(userProfileService.getUserProfile());
    }

    @PostMapping("/avatar")
    public ResponseEntity<UserProfileResponse> updateUserProfile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userProfileService.uploadUserPhoto(file));
    }

    @PutMapping("/data")
    public ResponseEntity<UserProfileResponse> updateUserData(@RequestBody UpdateUserData request) {
        return ResponseEntity.ok(userProfileService.updateUserProfile(request));
    }

}
