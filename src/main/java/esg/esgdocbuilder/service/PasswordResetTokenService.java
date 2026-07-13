package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.request.ForgotPasswordRequest;
import esg.esgdocbuilder.model.dto.request.ResetPasswordRequest;

public interface PasswordResetTokenService {
    void createPasswordResetToken(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}
