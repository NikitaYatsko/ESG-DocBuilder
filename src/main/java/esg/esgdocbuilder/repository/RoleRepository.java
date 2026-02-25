package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
