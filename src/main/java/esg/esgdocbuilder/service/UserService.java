package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.UserRegistrationRequest;
import esg.esgdocbuilder.model.dto.response.UserProfileResponse;

import java.util.List;

public interface UserService {
    UserProfileResponse createNewUser(UserRegistrationRequest userRegistrationRequest);
    List<UserProfileResponse> findAll();
}
