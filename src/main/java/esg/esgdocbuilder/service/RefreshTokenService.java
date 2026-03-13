package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.entity.RefreshToken;
import esg.esgdocbuilder.model.entity.User;

public interface RefreshTokenService {
    RefreshToken generateOrUpdateRefreshToken(User user);
    RefreshToken validateAndRefreshToken(String refreshToken);
}
