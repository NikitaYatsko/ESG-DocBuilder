package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.UpdateRoleRequest;
import esg.esgdocbuilder.model.dto.request.UserRegistrationRequest;
import esg.esgdocbuilder.model.dto.response.UserProfileResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserProfileResponse createNewUser(UserRegistrationRequest userRegistrationRequest);
    List<UserProfileResponse> findAll();
    void deleteUser(String email);
    void updateUserRole(Long userId, UpdateRoleRequest roleRequest);

}
