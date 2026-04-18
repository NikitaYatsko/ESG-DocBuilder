package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.BankOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BankOperationRepository extends JpaRepository<BankOperation, Long> {

    @EntityGraph(attributePaths = {"account"})
    Page<BankOperation> findAllByIsDeletedFalse(Pageable pageable);

}
