package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.LoginRequest;
import esg.esgdocbuilder.model.dto.response.UserProfileDTO;

public interface AuthService
{
    UserProfileDTO login(LoginRequest request);
}
