package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.dto.response.BankOperationResponse;
import esg.esgdocbuilder.model.dto.response.PaginationResponse;
import esg.esgdocbuilder.model.entity.BankOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BankOperationRepository extends JpaRepository<BankOperation, Long> {

    @EntityGraph(attributePaths = {"account"})
    Page<BankOperation> findAllByIsDeletedFalse(Pageable pageable);
  
@EntityGraph(attributePaths = {"account"})
    Page<BankOperation> findByIsDeletedFalseAndCreatedAtBetween(
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );

}
