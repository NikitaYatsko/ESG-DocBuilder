package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.model.dto.request.LoginRequest;
import esg.esgdocbuilder.model.dto.response.UserProfileDTO;
import esg.esgdocbuilder.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public UserProfileDTO login(LoginRequest request) {
        return null;
    }
}
