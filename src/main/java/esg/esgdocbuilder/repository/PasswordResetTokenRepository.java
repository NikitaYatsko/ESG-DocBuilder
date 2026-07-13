package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.PasswordResetToken;
import esg.esgdocbuilder.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
}
