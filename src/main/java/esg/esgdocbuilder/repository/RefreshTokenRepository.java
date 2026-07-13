package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.RefreshToken;
import esg.esgdocbuilder.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
