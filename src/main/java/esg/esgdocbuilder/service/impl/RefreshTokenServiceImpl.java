package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.exception.exceptions.RefreshTokenNotFoundException;
import esg.esgdocbuilder.model.entity.RefreshToken;
import esg.esgdocbuilder.model.entity.User;
import esg.esgdocbuilder.repository.RefreshTokenRepository;
import esg.esgdocbuilder.service.RefreshTokenService;
import esg.esgdocbuilder.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateOrUpdateRefreshToken(User user) {
        return refreshTokenRepository.findByUserId(user.getId()).map(
                refreshToken -> {
                    refreshToken.setCreated(LocalDateTime.now());
                    refreshToken.setToken(ApiUtils.generateUuidWithoutDash());
                    return refreshTokenRepository.save(refreshToken);
                }).orElseGet(() -> {
            RefreshToken newToken = new RefreshToken();
            newToken.setUser(user);
            newToken.setCreated(LocalDateTime.now());
            newToken.setToken(ApiUtils.generateUuidWithoutDash());
            return refreshTokenRepository.save(newToken);
        });
    }

    @Override
    public RefreshToken validateAndRefreshToken(String rToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(rToken).orElseThrow(
                () -> new RefreshTokenNotFoundException(ApiErrorMessage.REFRESH_TOKEN_NOT_FOUND.getMessage())
        );
        refreshToken.setCreated(LocalDateTime.now());
        refreshToken.setToken(ApiUtils.generateUuidWithoutDash());
        return refreshTokenRepository.save(refreshToken);
    }
}
