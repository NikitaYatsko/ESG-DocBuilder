package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.BankOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankOperationRepository extends JpaRepository<BankOperation, Long> {
}
