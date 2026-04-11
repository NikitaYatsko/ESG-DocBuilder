package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @EntityGraph(attributePaths = {"roles"})
    List<User> findAll();

    boolean existsByEmail(String email);
}
