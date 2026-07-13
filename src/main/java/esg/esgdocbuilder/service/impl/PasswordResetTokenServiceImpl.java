package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.exception.exceptions.InvalidResetTokenException;
import esg.esgdocbuilder.exception.exceptions.UserDoesNotExistsException;
import esg.esgdocbuilder.model.dto.request.ForgotPasswordRequest;
import esg.esgdocbuilder.model.dto.request.ResetPasswordRequest;
import esg.esgdocbuilder.model.entity.PasswordResetToken;
import esg.esgdocbuilder.model.entity.User;
import esg.esgdocbuilder.repository.PasswordResetTokenRepository;
import esg.esgdocbuilder.repository.RefreshTokenRepository;
import esg.esgdocbuilder.repository.UserRepository;
import esg.esgdocbuilder.service.PasswordResetTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void createPasswordResetToken(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UserDoesNotExistsException(ApiErrorMessage.USER_DOES_NOT_EXIST.getMessage())
        );
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        passwordResetTokenRepository.save(passwordResetToken);


    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(request.getToken()).orElseThrow(
                () -> new InvalidResetTokenException(ApiErrorMessage.INVALID_RESET_TOKEN.getMessage())
        );

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidResetTokenException(ApiErrorMessage.INVALID_RESET_TOKEN.getMessage());
        }
        if (token.isUsed()) {
            throw new InvalidResetTokenException(ApiErrorMessage.INVALID_RESET_TOKEN.getMessage());
        }
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        refreshTokenRepository.deleteByUser(user);
        token.setUsed(true);
        passwordResetTokenRepository.save(token);

    }
}
