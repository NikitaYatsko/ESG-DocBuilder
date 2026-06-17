package esg.esgdocbuilder.repository;

import esg.esgdocbuilder.model.entity.BankingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankingCategoryRepository extends JpaRepository<BankingCategory, Long> {
}