package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.LoginRequest;
import esg.esgdocbuilder.model.dto.response.UserAuthResponse;

public interface AuthService
{
    UserAuthResponse login(LoginRequest request);
}
