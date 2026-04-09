package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.BankOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankOperationRepository extends JpaRepository<BankOperation, Long> {
    @Override
    @EntityGraph(attributePaths = {"account"})
    Page<BankOperation> findAll(Pageable pageable);
}
